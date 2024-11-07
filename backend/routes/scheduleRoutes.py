from fastapi import APIRouter, Depends
from schemas.schedule import Schedule, ScheduleOut
from config.db import get_db
from sqlalchemy.orm import Session
from security.seguridad import Portador
from controllers.scheduleController import create_schedule, get_schedule_by_id, get_all_schedules, update_schedule, delete_schedule

router = APIRouter()

# Create a schedule
@router.post("/new_schedule/", dependencies=[Depends(Portador())])
def create_schedule_route(schedule: Schedule, db: Session = Depends(get_db)):
    new_schedule= create_schedule(schedule, db)
    return ScheduleOut(**new_schedule.__dict__)

# Get a schedule by id
@router.get("/schedule/{schedule_id}", dependencies=[Depends(Portador())])
def get_schedule_by_id_route(schedule_id: int, db: Session = Depends(get_db)):
    schedule = get_schedule_by_id(schedule_id, db)
    return schedule

# Get all schedules
@router.get("/all_schedule/", dependencies=[Depends(Portador())])
def get_all_schedules_route(db: Session = Depends(get_db)):
    schedules = get_all_schedules(db)
    return schedules

# Update a schedule
@router.put("/schedule/{schedule_id}", dependencies=[Depends(Portador())])
def update_schedule_route(schedule_id: int, schedule: Schedule, db: Session = Depends(get_db)):
    updated_schedule = update_schedule(schedule_id, schedule, db)
    return updated_schedule

# Delete a schedule
@router.delete("/schedule/{schedule_id}", dependencies=[Depends(Portador())])
def delete_schedule_route(schedule_id: int, db: Session = Depends(get_db)):
    schedule = delete_schedule(schedule_id, db)
    return schedule