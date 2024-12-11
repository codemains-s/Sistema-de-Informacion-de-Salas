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
    # Verificar que el usuario exista
    user = exist_user_id(new_room_booking.user_id, db)
    if not user:
        return "Usuario ingresado no existe"

    # Verificar que la sala existe
    room = db.query(Room).filter(Room.id == new_room_booking.room_id).first()
    if not room:
        return "La sala seleccionada no existe"

    # Validar horarios de la reserva
    validation_message = validate_booking_times(new_room_booking.start_time, new_room_booking.end_time)
    if validation_message != "Horas de monitorias son válidas":
        return validation_message

    # Validar la fecha de la reserva
    validation_message = validate_booking_date(new_room_booking.booking_date)
    if validation_message != "Fecha válida":
        return validation_message

    # Verificar que el horario específico esté disponible
    schedule = db.query(RoomSchedule).filter(
        RoomSchedule.room_id == new_room_booking.room_id,
        RoomSchedule.hour_start <= new_room_booking.start_time,
        RoomSchedule.hour_end >= new_room_booking.end_time,
        RoomSchedule.status == "Disponible"
    ).first()

    if not schedule:
        return "El horario solicitado no está disponible."

    # Crear la reserva
    db_room_booking = RoomBooking(**new_room_booking.dict())
    db.add(db_room_booking)

    # Actualizar el estado del horario a "Reservado"
    schedule.status = "Reservado"

    # Actualizar el estado de la sala si todos los horarios están reservados
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
    return db.query(RoomBooking).all()

def get_room_booking_by_id(id: int, db):
    return db.query(RoomBooking).filter(RoomBooking.id == id).first()

def get_room_bookings_by_user_id(user_id: int, db):
    room_bookings = db.query(RoomBooking).filter(RoomBooking.user_id == user_id).all()

    if not room_bookings:
        return f"No se encontraron reservas para el usuario con ID {user_id}."

    return room_bookings


def update_room_booking(id: int, new_room_booking: RoomBooking, db):
    # Validar horarios y fecha de la reserva
    validate_booking_times(new_room_booking.start_time, new_room_booking.end_time)
    validate_booking_date(new_room_booking.booking_date)

    # Actualizar la reserva
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
    # Buscar la reserva
    room_booking = db.query(RoomBooking).filter(RoomBooking.id == id).first()
    if not room_booking:
        return "Reserva no encontrada"

    # Liberar el horario asociado cambiando su estado a "Disponible"
    schedule = db.query(RoomSchedule).filter(
        RoomSchedule.room_id == room_booking.room_id,
        RoomSchedule.hour_start <= room_booking.start_time,
        RoomSchedule.hour_end >= room_booking.end_time
    ).first()

    if schedule:
        schedule.status = "Disponible"

    # Eliminar la reserva
    db.delete(room_booking)

    # Verificar si todos los horarios están disponibles para cambiar el estado de la sala
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
    return db.query(RoomBooking).filter(RoomBooking.id == id).first()

def validate_booking_times(start_time: str, end_time: str):
    """Valida que los horarios sean correctos."""
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
    """Valida que la fecha de reserva no sea un día pasado."""
    try:
        booking_date = datetime.strptime(booking_date, "%d/%m/%Y").date()
    except ValueError:
        return "Formato de fecha inválido. Debe ser DD/MM/YYYY."

    today = date.today()
    if booking_date < today:
        return "Fecha no válida"
    return "Fecha válida"
