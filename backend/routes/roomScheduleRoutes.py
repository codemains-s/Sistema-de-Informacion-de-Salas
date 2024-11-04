from fastapi import APIRouter, Depends
from schemas.roomSchedules import RoomSchedule, RoomScheduleOut
from config.db import get_db
from sqlalchemy.orm import Session
from controllers.roomScheduleController import create_room_schedule, all_room_schedules, get_room_schedule_by_id, update_room_schedule, delete_room_schedule

router = APIRouter()

# Create a room schedule
@router.post("/new_roomSchedule/")
def create_room_schedule_route(room_schedule: RoomSchedule, db: Session = Depends(get_db)):
    new_room_schedule = create_room_schedule(room_schedule, db)
    return RoomSchedule(**new_room_schedule.__dict__)

# Get all room schedules
@router.get("/all_roomSchedule/")
def all_room_schedules_route(db: Session = Depends(get_db)):
    room_schedules = all_room_schedules(db)
    return room_schedules

# Get room schedule by id
@router.get("/roomSchedule/{id}")
def get_room_schedule_by_id_route(id: int, db: Session = Depends(get_db)):
    room_schedule = get_room_schedule_by_id(id, db)
    return room_schedule

# Update room schedule
@router.put("/roomSchedule/{id}")
def update_room_schedule_route(id: int, updated_room_schedule: RoomSchedule, db: Session = Depends(get_db)):
    room_schedule = update_room_schedule(id, updated_room_schedule, db)
    return room_schedule

# Delete room schedule
@router.delete("/roomSchedule/{id}")
def delete_room_schedule_route(id: int, db: Session = Depends(get_db)):
    room_schedule = delete_room_schedule(id, db)
    return room_schedule
