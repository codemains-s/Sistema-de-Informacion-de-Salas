from schemas.role import Role
from models.tables import *
from sqlalchemy import func, text


def create_role(new_role: Role, db):
    db_role = Role(**new_role.dict())
    db.add(db_role)
    db.commit()
    db.refresh(db_role)
    return db_role

def all_roles(db):
    return db.query(Role).all()

def get_role_by_id(id: int, db):
    return db.query(Role).filter(Role.id == id).first()

def get_role_by_name(name:str, db):
    rol = db.query(Role).filter(Role.name == name).first()
    return rol.id

def update_role(id_role, updated_role: Role, db):
    db_role = db.query(Role).filter(Role.id == id_role).first()
    db_role.name = updated_role.name
    db.commit()
    db.refresh(db_role)
    return db_role

def delete_role(id: int, db):
    db_role = db.query(Role).filter(Role.id == id).first()
    db.delete(db_role)
    db.commit()
    role_count = db.query(Role).count()
    if role_count == 0:
        db.execute(text("ALTER TABLE roles AUTO_INCREMENT = 1"))
    else:
        max_id = db.query(func.max(Role.id)).scalar()
        db.execute(text(f"ALTER TABLE roles AUTO_INCREMENT = {max_id + 1}"))
    db.commit()
    return db_role

def exist_role(name:str, db):
    role = db.query(Role).filter(func.upper(Role.name) == name.upper()).first()
    return role