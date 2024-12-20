from schemas.roomSchedule import RoomSchedule
from models.tables import *
from sqlalchemy import func, text


def create_room_schedule(room_shedule: RoomSchedule, db):
    new_room_schedule = RoomSchedule(**room_shedule.dict())
    db.add(new_room_schedule)
    db.commit()
    db.refresh(new_room_schedule)
    return new_room_schedule

def all_room_schedules(db):
    return db.query(RoomSchedule).all()

def get_room_schedule_by_id(id: int, db):
    return db.query(RoomSchedule).filter(RoomSchedule.id == id).first()

def get_room_schedule_by_room_id(room_id: int, db):
    return db.query(RoomSchedule).filter(RoomSchedule.room_id == room_id).all()


def update_room_schedule(id_room_schedule: int, updated_room_schedule: RoomSchedule, db):
    # Buscar el horario
    db_room_schedule = db.query(RoomSchedule).filter(RoomSchedule.id == id_room_schedule).first()
    if not db_room_schedule:
        return "Horario no encontrado"

    # Actualizar los detalles del horario
    db_room_schedule.room_id = updated_room_schedule.room_id
    db_room_schedule.hour_start = updated_room_schedule.hour_start
    db_room_schedule.hour_end = updated_room_schedule.hour_end
    db_room_schedule.day_of_week = updated_room_schedule.day_of_week
    db_room_schedule.status = updated_room_schedule.status

    db.commit()
    db.refresh(db_room_schedule)
    return db_room_schedule


def delete_room_schedule(id_room_schedule: int, db):
    db_room_schedule = db.query(RoomSchedule).filter(RoomSchedule.id == id_room_schedule).first()
    db.delete(db_room_schedule)
    db.commit()
    room_schedule_count = db.query(RoomSchedule).count()
    if room_schedule_count == 0:
        db.execute(text("ALTER TABLE roomschedules AUTO_INCREMENT = 1"))
    else:
        max_id = db.query(func.max(RoomSchedule.id)).scalar()
        db.execute(text(f"ALTER TABLE roomschedules AUTO_INCREMENT = {max_id + 1}"))
    db.commit()

    return db_room_schedule

