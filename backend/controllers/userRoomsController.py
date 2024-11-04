from schemas.userRooms import UserRoom
from models.tables import *

def create_user_rooms(user_room: UserRoom, db):
    new_user_room = UserRoom(**user_room.dict())
    db.add(new_user_room)
    db.commit()
    db.refresh(new_user_room)
    return new_user_room

def get_all_user_rooms(db):
    return db.query(UserRoom).all()

def get_user_room_by_id(id: int, db):
    return db.query(UserRoom).filter(UserRoom.id == id).first()

def update_user_room(id: int, user_room: UserRoom, db):
    user_room_to_update = db.query(UserRoom).filter(UserRoom.id == id).first()
    user_room_to_update.id_user = user_room.id_user
    user_room_to_update.id_room = user_room.id_room
    user_room_to_update.date = user_room.date
    user_room_to_update.hour = user_room.hour
    db.commit()
    db.refresh(user_room_to_update)
    return user_room_to_update

def delete_user_room(id: int, db):
    user_room_to_delete = db.query(UserRoom).filter(UserRoom.id == id).first()
    db.delete(user_room_to_delete)
    db.commit()
    return user_room_to_delete