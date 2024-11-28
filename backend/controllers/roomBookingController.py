from schemas.roomBooking import RoomBooking, RoomBookingOut
from models.tables import *
from sqlalchemy import func, text
from datetime import datetime, time, date
from fastapi import HTTPException
from controllers.userController import exist_user_id

def create_room_booking(new_room_booking: RoomBooking, db):
    # Verificar que el usuario exista
    user = exist_user_id(new_room_booking.user_id, db)
    if not user:
        return "Usuario ingresado no existe"

    validation_message = validate_booking_times(new_room_booking.start_time, new_room_booking.end_time)
    if validation_message != "Horas de monitorias son válidas":
        return validation_message

    validation_message = validate_booking_date(new_room_booking.booking_date)
    if validation_message != "Fecha válida":
        return validation_message

    db_room_booking = RoomBooking(**new_room_booking.dict())
    db.add(db_room_booking)
    db.commit()
    db.refresh(db_room_booking)
    return db_room_booking


def all_room_bookings(db):
    salida = db.query(RoomBooking).all()
    return salida


def get_room_booking_by_id(id: int, db):
    return db.query(RoomBooking).filter(RoomBooking.id == id).first()

def update_room_booking(id: int, new_room_booking: RoomBooking, db):
    validate_booking_times(new_room_booking.start_time, new_room_booking.end_time)
    
    validate_booking_date(new_room_booking.booking_date)

    db_room_booking = db.query(RoomBooking).filter(RoomBooking.id == id).first()
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
    room_booking = db.query(RoomBooking).filter(RoomBooking.id == id).first()
    db.delete(room_booking)
    db.commit()
    booking_count = db.query(RoomBooking).count()
    if booking_count == 0:
        db.execute(text("ALTER TABLE room_bookings AUTO_INCREMENT = 1"))
    else:
        max_id = db.query(func.max(RoomBooking.id)).scalar()
        db.execute(text(f"ALTER TABLE room_bookings AUTO_INCREMENT = {max_id + 1}"))
    db.commit()
    return room_booking

def exist_room_booking(id: int, db):
    return db.query(RoomBooking).filter(RoomBooking.id == id).first()

def validate_booking_times(start_time: str, end_time: str):
    """Valida que los horarios sean correctos:
       - end_time no sea anterior o igual a start_time
       - Ambos horarios estén entre 6:00 y 22:00 (formato 24 horas)
    """
    valid_start_time = time(6, 0)
    valid_end_time = time(22, 0)

    try:
        start_dt = datetime.strptime(start_time, "%H:%M").time()
        end_dt = datetime.strptime(end_time, "%H:%M").time()
    except ValueError:
        return "Formato de hora inválido. Debe ser HH:MM (24 horas)."

    if start_dt < valid_start_time or start_dt > valid_end_time:
        return "Horas de monitorias son inválidas"
    if end_dt < valid_start_time or end_dt > valid_end_time:
        return "Horas de monitorias son inválidas"

    if end_dt <= start_dt:
        return "Horas de monitorias son inválidas"

    return "Horas de monitorias son válidas"


def validate_booking_date(booking_date: date):
    """Valida que la fecha de reserva no sea un día pasado."""
    booking_date = datetime.strptime(booking_date, "%d/%m/%Y").date()
    today = date.today()
    if booking_date < today:
        return "Fecha no válida"
    return "Fecha válida"