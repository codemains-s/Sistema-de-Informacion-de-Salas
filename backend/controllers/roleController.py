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
    return db.query(Role).filter(Role.name == name).first()

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
    return db_role

def exist_role(name:str, db):
    return db.query(Role).filter(func.upper(Role.name) == name.upper()).first() is not None