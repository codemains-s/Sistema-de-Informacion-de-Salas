from schemas.completedHour import CompletedHour
from models.tables import *
from sqlalchemy import func, text
from fastapi import HTTPException


def create_completed_hour(new_completed_hour: CompletedHour, db):
    db_completed_hour = CompletedHours(**new_completed_hour.model_dump())
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
    db_completed_hour.date_register = updated_completed_hour.date_register
    db_completed_hour.status = updated_completed_hour.status
    db_completed_hour.observations = updated_completed_hour.observations
    db.commit()
    db.refresh(db_completed_hour)
    return db_completed_hour

from sqlalchemy.orm import Session
from fastapi import HTTPException

def delete_completed_hour(id: int, db: Session):
    # Buscar el registro por ID
    completed_hour = db.query(CompletedHours).filter(CompletedHours.id == id).first()
    if completed_hour is None:
        raise HTTPException(status_code=404, detail="The completed hour does not exist")
    
    # Eliminar el registro
    db.delete(completed_hour)
    db.commit()
    return None  # Ahora se devuelve None en lugar del objeto eliminado



def exist_completed_hour(id: int, db):
    return db.query(CompletedHours).filter(CompletedHours.id == id).first()
