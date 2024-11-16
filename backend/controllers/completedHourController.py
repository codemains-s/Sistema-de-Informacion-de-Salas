from schemas.completedHour import CompletedHour
from models.tables import *
from sqlalchemy import func, text


def create_completed_hour(new_completed_hour: CompletedHour, db):
    db_completed_hour = CompletedHours(**new_completed_hour.dict())
    db.add(db_completed_hour)
    db.commit()
    db.refresh(db_completed_hour)
    return db_completed_hour

def all_completed_hours(db):
    return db.query(CompletedHours).all()

def get_completed_hour_by_id(id: int, db):
    return db.query(CompletedHours).filter(CompletedHours.id == id).first()

def get_completed_hour_by_user_id(user_id: int, db):
    return db.query(CompletedHours).filter(CompletedHours.user_id == user_id).all()

def update_completed_hour(id_completed_hour, updated_completed_hour: CompletedHour, db):
    db_completed_hour = db.query(CompletedHours).filter(CompletedHours.id == id_completed_hour).first()
    db_completed_hour.room_booking_id = updated_completed_hour.room_booking_id
    db_completed_hour.hour_completed = updated_completed_hour.hour_completed
    db_completed_hour.date_registered = updated_completed_hour.date_registered
    db_completed_hour.status = updated_completed_hour.status
    db_completed_hour.observation = updated_completed_hour.observation
    db.commit()
    db.refresh(db_completed_hour)
    return db_completed_hour

def delete_completed_hour(id: int, db):
    completed_hour = db.query(CompletedHours).filter(CompletedHours.id == id).first()
    db.delete(completed_hour)
    db.commit()
    hour_count = db.query(CompletedHour).count()
    if hour_count == 0:
        db.execute("ALTER TABLE completed_hours AUTO_INCREMENT = 1")
    else:
        db.execute("ALTER TABLE completed_hours AUTO_INCREMENT = {}".format(hour_count + 1))
    db.commit()
    return completed_hour

def exist_completed_hour(id: int, db):
    return db.query(CompletedHours).filter(CompletedHours.id == id).first()
