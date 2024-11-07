from sqlalchemy import func, text
from schemas.room import Room
from models.tables import *
from typing import List, Optional

def create_room(new_room:Room, db):
    db_room = Room(**new_room.dict())
    db.add(db_room)
    db.commit()
    db.refresh(db_room)
    return db_room

def all_rooms(db):
    return db.query(Room).all()

def get_room_by_id(id, db):
    return db.query(Room).filter(Room.id == id).first()

def get_room_by_name(name: str, db):
    return db.query(Room).filter(Room.name == name).first()

def update_room(id_room: int, updated_room: Room, db)->Optional[Room]:
    db_room = db.query(Room).filter(Room.id == id_room).first()
    db_room.name = updated_room.name
    db_room.address = updated_room.address
    db_room.description = updated_room.description
    db_room.capacity = updated_room.capacity
    db_room.status = updated_room.status
    db_room.image = updated_room.image
    db.commit()
    db.refresh(db_room)
    return db_room

def delete_room(id: int, db):
    db_room = db.query(Room).filter(Room.id == id).first()
    db.delete(db_room)
    db.commit()
    room_count = db.query(Room).count()
    if room_count == 0:
        db.execute(text("ALTER TABLE rooms AUTO_INCREMENT = 1"))
    else:
        max_id = db.query(func.max(Room.id)).scalar()
        db.execute(text(f"ALTER TABLE rooms AUTO_INCREMENT = {max_id + 1}"))
    db.commit()

    return db_room

def exist_room(name: str, db):
    return db.query(Room).filter(func.upper(Room.name) == name.upper()).first()