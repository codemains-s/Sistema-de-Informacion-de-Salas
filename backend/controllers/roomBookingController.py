from schemas.roomBooking import RoomBooking
from models.tables import *
from sqlalchemy import func, text

def create_room_booking(new_room_booking: RoomBooking, db):
    db_room_booking = RoomBooking(**new_room_booking.dict())
    db.add(db_room_booking)
    db.commit()
    db.refresh(db_room_booking)
    return db_room_booking

def all_room_bookings(db):
    return db.query(RoomBooking).all()

def get_room_booking_by_id(id: int, db):
    return db.query(RoomBooking).filter(RoomBooking.id == id).first()

def update_room_booking(id: int, new_room_booking: RoomBooking, db):
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