from schemas.schedule import Schedule
from models.tables import *
from sqlalchemy import func, text

def create_schedule(schedule: Schedule, db):
    new_schedules = Schedule(**schedule.dict())
    db.add(new_schedules)
    db.commit()
    db.refresh(new_schedules)
    return new_schedules

def get_schedule_by_id(schedule_id: int, db):
    schedules = db.query(Schedule).filter(Schedule.id == schedule_id).first()
    return schedules

def get_all_schedules(db):
    schedules = db.query(Schedule).all()
    return schedules

def update_schedule(schedule_id: int, schedule: Schedule, db):
    schedules = db.query(Schedule).filter(Schedule.id == schedule_id).first()
    schedules.day = schedule.day
    schedules.start_time = schedule.start_time
    schedules.end_time = schedule.end_time
    db.commit()
    db.refresh(schedules)
    return schedules

def delete_schedule(schedule_id: int, db):
    schedules = db.query(Schedule).filter(Schedule.id == schedule_id).first()
    db.delete(schedules)
    db.commit()
    schedule_count = db.query(Schedule).count()
    if schedule_count == 0:
        db.execute(text("ALTER TABLE schedules AUTO_INCREMENT = 1"))
    else:
        max_id = db.query(func.max(Schedule.id)).scalar()
        db.execute(text(f"ALTER TABLE schedules AUTO_INCREMENT = {max_id + 1}"))
    db.commit()
    return schedules