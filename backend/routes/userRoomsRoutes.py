from fastapi import APIRouter, Depends
from schemas.userRooms import UserRoom, UserRoomOut
from config.db import get_db
from sqlalchemy.orm import Session
from security.seguridad import Portador
from controllers.userRoomsController import create_user_rooms, get_all_user_rooms, get_user_room_by_id, update_user_room, delete_user_room

router = APIRouter()

# Create a user room
@router.post("/user_room/", dependencies=[Depends(Portador())])
def create_user_room_route(user_room: UserRoom, db: Session = Depends(get_db)):
    new_user_room = create_user_rooms(user_room, db)
    return UserRoom(**new_user_room.__dict__)

# Get a user room by id
@router.get("/user_room/{user_room_id}", dependencies=[Depends(Portador())])
def get_user_room_by_id_route(user_room_id: int, db: Session = Depends(get_db)):
    user_room = get_user_room_by_id(user_room_id, db)
    return user_room

# Get all user rooms
@router.get("/all_user_room/", dependencies=[Depends(Portador())])
def get_all_user_rooms_route(db: Session = Depends(get_db)):
    user_rooms = get_all_user_rooms(db)
    return user_rooms

# Update a user room
@router.put("/user_room/{user_room_id}", dependencies=[Depends(Portador())])
def update_user_room_route(user_room_id: int, user_room: UserRoom, db: Session = Depends(get_db)):
    updated_user_room = update_user_room(user_room_id, user_room, db)
    return updated_user_room

# Delete a user room
@router.delete("/user_room/{user_room_id}", dependencies=[Depends(Portador())])
def delete_user_room_route(user_room_id: int, db: Session = Depends(get_db)):
    user_room = delete_user_room(user_room_id, db)
    return user_room