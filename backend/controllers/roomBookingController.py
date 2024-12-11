from schemas.roomBooking import RoomBooking, RoomBookingOut
from schemas.room import Room
from models.tables import *
from sqlalchemy import func, text
from datetime import datetime, time, date
from fastapi import HTTPException
from controllers.userController import exist_user_id
from controllers.roomScheduleController import all_room_schedules, get_room_schedule_by_room_id
from collections import deque

deleted_ids = deque()

def create_room_booking(new_room_booking: RoomBooking, db):
    """
    Crea una nueva reserva de sala en la base de datos.

    Args:
        new_room_booking (RoomBooking): Objeto RoomBooking con los datos de la nueva reserva
        db: Sesión de la base de datos

    Returns:
        RoomBooking | str: El objeto RoomBooking creado si es exitoso, o un mensaje de error si falla

    La función realiza las siguientes validaciones:
        - Verifica que exista el usuario
        - Verifica que exista la sala
        - Valida las horas de inicio y fin
        - Valida la fecha de reserva
        - Verifica disponibilidad del horario
        
    También actualiza:
        - El estado del horario a "Reservado"
        - El estado de la sala según disponibilidad restante
    """
    user = exist_user_id(new_room_booking.user_id, db)
    if not user:
        return "Usuario ingresado no existe"

    room = db.query(Room).filter(Room.id == new_room_booking.room_id).first()
    if not room:
        return "La sala seleccionada no existe"

    validation_message = validate_booking_times(new_room_booking.start_time, new_room_booking.end_time)
    if validation_message != "Horas de monitorias son válidas":
        return validation_message

    validation_message = validate_booking_date(new_room_booking.booking_date)
    if validation_message != "Fecha válida":
        return validation_message

    schedule = db.query(RoomSchedule).filter(
        RoomSchedule.room_id == new_room_booking.room_id,
        RoomSchedule.hour_start <= new_room_booking.start_time,
        RoomSchedule.hour_end >= new_room_booking.end_time,
        RoomSchedule.status == "Disponible"
    ).first()

    if not schedule:
        return "El horario solicitado no está disponible."

    db_room_booking = RoomBooking(**new_room_booking.dict())
    db.add(db_room_booking)

    schedule.status = "Reservado"

    remaining_schedules = db.query(RoomSchedule).filter(
        RoomSchedule.room_id == new_room_booking.room_id,
        RoomSchedule.status == "Disponible"
    ).all()

    if not remaining_schedules:
        room.status = "Reservada"
    else:
        room.status = "Disponible"

    db.commit()
    db.refresh(db_room_booking)
    return db_room_booking


def all_room_bookings(db):
    """
    Obtiene todas las reservas de salas almacenadas en la base de datos.

    Args:
        db: Sesión de la base de datos

    Returns:
        list: Lista de todos los objetos RoomBooking en la base de datos
    """
    return db.query(RoomBooking).all()

def get_room_booking_by_id(id: int, db):
    """
    Obtiene una reserva de sala por su ID de la base de datos.

    Args:
        id (int): ID de la reserva a buscar
        db: Sesión de la base de datos

    Returns:
        RoomBooking: Objeto RoomBooking encontrado o None si no existe
    """
    return db.query(RoomBooking).filter(RoomBooking.id == id).first()

def get_room_bookings_by_user_id(user_id: int, db):
    """
    Obtiene todas las reservas de sala asociadas a un usuario específico.

    Args:
        user_id (int): ID del usuario del cual se quieren obtener las reservas
        db: Sesión de la base de datos

    Returns:
        list: Lista de objetos RoomBooking si se encuentran reservas
        str: Mensaje indicando que no se encontraron reservas si no hay ninguna
    """
    room_bookings = db.query(RoomBooking).filter(RoomBooking.user_id == user_id).all()

    if not room_bookings:
        return f"No se encontraron reservas para el usuario con ID {user_id}."

    return room_bookings


def update_room_booking(id: int, new_room_booking: RoomBooking, db):
    """
    Actualiza una reserva de sala existente en la base de datos.

    Args:
        id (int): ID de la reserva a actualizar
        new_room_booking (RoomBooking): Objeto RoomBooking con los nuevos datos de la reserva
        db: Sesión de la base de datos

    Returns:
        RoomBooking: El objeto RoomBooking actualizado si la actualización fue exitosa
        str: Mensaje de error si la reserva no fue encontrada

    Note:
        Esta función valida los horarios y fechas de la reserva antes de actualizarla.
        Actualiza todos los campos de la reserva incluyendo:
        - ID del usuario
        - ID de la sala
        - Hora de inicio
        - Hora de fin 
        - Estado
        - Fecha de reserva
    """
    validate_booking_times(new_room_booking.start_time, new_room_booking.end_time)
    validate_booking_date(new_room_booking.booking_date)

    db_room_booking = db.query(RoomBooking).filter(RoomBooking.id == id).first()
    if not db_room_booking:
        return "Reserva no encontrada"
    
    db_room_booking.user_id = new_room_booking.user_id
    db_room_booking.room_id = new_room_booking.room_id
    db_room_booking.start_time = new_room_booking.start_time
    db_room_booking.end_time = new_room_booking.end_time
    db_room_booking.status = new_room_booking.status
    db_room_booking.booking_date = new_room_booking.booking_date

    db.commit()
    db.refresh(db_room_booking)
    return db_room_booking


def delete_room_booking(id: int, db):
    """
    Elimina una reserva de sala y actualiza los estados relacionados.

    Args:
        id (int): ID de la reserva a eliminar
        db: Sesión de la base de datos

    Returns:
        RoomBooking: El objeto RoomBooking eliminado si la operación fue exitosa
        str: Mensaje de error si la reserva no fue encontrada

    Note:
        Esta función realiza las siguientes operaciones:
        1. Busca y elimina la reserva de sala
        2. Actualiza el estado del horario asociado a "Disponible"
        3. Actualiza el estado de la sala según los horarios restantes
        4. Confirma los cambios en la base de datos
    """
    room_booking = db.query(RoomBooking).filter(RoomBooking.id == id).first()
    if not room_booking:
        return "Reserva no encontrada"

    schedule = db.query(RoomSchedule).filter(
        RoomSchedule.room_id == room_booking.room_id,
        RoomSchedule.hour_start <= room_booking.start_time,
        RoomSchedule.hour_end >= room_booking.end_time
    ).first()

    if schedule:
        schedule.status = "Disponible"

    db.delete(room_booking)

    room = db.query(Room).filter(Room.id == room_booking.room_id).first()
    remaining_schedules = db.query(RoomSchedule).filter(
        RoomSchedule.room_id == room_booking.room_id,
        RoomSchedule.status == "Disponible"
    ).all()

    if not remaining_schedules:
        room.status = "Reservada"
    else:
        room.status = "Disponible"

    db.commit()
    return room_booking

def exist_room_booking(id: int, db):
    """
    Verifica si existe una reserva de sala con el ID especificado.

    Args:
        id (int): ID de la reserva a buscar
        db: Sesión de la base de datos

    Returns:
        RoomBooking: Objeto RoomBooking si existe, None si no existe
    """
    return db.query(RoomBooking).filter(RoomBooking.id == id).first()

def validate_booking_times(start_time: str, end_time: str):
    """
    Valida que los horarios de reserva estén dentro del rango permitido y tengan el formato correcto.

    Args:
        start_time (str): Hora de inicio en formato HH:MM (24 horas)
        end_time (str): Hora de fin en formato HH:MM (24 horas)

    Returns:
        str: Mensaje indicando si las horas son válidas o no. Posibles valores:
            - "Horas de monitorias son válidas": Si los horarios son correctos
            - "Horas de monitorias son inválidas": Si los horarios están fuera del rango permitido
            - "Formato de hora inválido. Debe ser HH:MM (24 horas)": Si el formato de hora es incorrecto

    Note:
        El rango de horas válido es de 6:00 a 22:00.
        La hora de fin debe ser posterior a la hora de inicio.
    """
    valid_start_time = time(6, 0)
    valid_end_time = time(22, 0)
    mensaje_retorno = "Horas de monitorias son inválidas"

    try:
        start_dt = datetime.strptime(start_time, "%H:%M").time()
        end_dt = datetime.strptime(end_time, "%H:%M").time()
    except ValueError:
        return "Formato de hora inválido. Debe ser HH:MM (24 horas)."

    if start_dt < valid_start_time or start_dt > valid_end_time:
        return mensaje_retorno
    if end_dt < valid_start_time or end_dt > valid_end_time:
        return mensaje_retorno
    if end_dt <= start_dt:
        return mensaje_retorno

    return "Horas de monitorias son válidas"

def validate_booking_date(booking_date: str):
    """
    Valida que la fecha de reserva no sea un día pasado.

    Args:
        booking_date (str): Fecha de reserva en formato DD/MM/YYYY

    Returns:
        str: Mensaje indicando si la fecha es válida o no. Posibles valores:
            - "Fecha válida": Si la fecha es igual o posterior a hoy
            - "Fecha no válida": Si la fecha es anterior a hoy
            - "Formato de fecha inválido. Debe ser DD/MM/YYYY.": Si el formato de fecha es incorrecto

    Note:
        La función verifica que la fecha proporcionada no sea anterior a la fecha actual
        y que tenga el formato correcto DD/MM/YYYY.
    """
    try:
        booking_date = datetime.strptime(booking_date, "%d/%m/%Y").date()
    except ValueError:
        return "Formato de fecha inválido. Debe ser DD/MM/YYYY."

    today = date.today()
    if booking_date < today:
        return "Fecha no válida"
    return "Fecha válida"
