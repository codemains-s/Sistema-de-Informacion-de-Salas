from schemas.completedHour import CompletedHour
from models.tables import *
from sqlalchemy import func, text
from fastapi import HTTPException
from sqlalchemy.orm import Session
from fastapi import HTTPException

def create_completed_hour(new_completed_hour: CompletedHour, db):
    """Crea un nuevo registro de hora completada en la base de datos.

    Args:
        new_completed_hour (CompletedHour): Objeto con los datos de la hora completada a crear.
        db: Sesión de la base de datos.

    Returns:
        CompletedHours: Objeto de hora completada creado y almacenado en la base de datos.

    Raises:
        SQLAlchemyError: Si ocurre un error durante la operación de base de datos.
    """
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
    """Actualiza un registro de hora completada existente en la base de datos.

    Args:
        id_completed_hour (int): ID del registro de hora completada a actualizar.
        updated_completed_hour (CompletedHour): Objeto con los nuevos datos de la hora completada.
        db: Sesión de la base de datos.

    Returns:
        CompletedHours: Objeto de hora completada actualizado.

    Raises:
        SQLAlchemyError: Si ocurre un error durante la operación de base de datos.
    """
    db_completed_hour = db.query(CompletedHours).filter(CompletedHours.id == id_completed_hour).first()
    db_completed_hour.room_booking_id = updated_completed_hour.room_booking_id
    db_completed_hour.hour_completed = updated_completed_hour.hour_completed
    db_completed_hour.date_register = updated_completed_hour.date_register
    db_completed_hour.status = updated_completed_hour.status
    db_completed_hour.signature = updated_completed_hour.signature
    db_completed_hour.observations = updated_completed_hour.observations
    db.commit()
    db.refresh(db_completed_hour)
    return db_completed_hour

def delete_completed_hour(id: int, db: Session):
    """Elimina un registro de hora completada de la base de datos.

    Args:
        id (int): ID del registro de hora completada a eliminar.
        db (Session): Sesión de la base de datos.

    Returns:
        None: No retorna ningún valor.

    Raises:
        HTTPException: Si el registro de hora completada no existe (código 404).
        SQLAlchemyError: Si ocurre un error durante la operación de base de datos.
    """
    completed_hour = db.query(CompletedHours).filter(CompletedHours.id == id).first()
    if completed_hour is None:
        raise HTTPException(status_code=404, detail="The completed hour does not exist")
    
    db.delete(completed_hour)
    db.commit()
    return None

def exist_completed_hour(id: int, db):
    return db.query(CompletedHours).filter(CompletedHours.id == id).first()
