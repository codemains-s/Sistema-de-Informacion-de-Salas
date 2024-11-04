from schemas.user import User
from models.tables import *
from sqlalchemy import func, text

def create_user(user: User, db):
    new_user = User(**user.dict())
    db.add(new_user)
    db.commit()
    db.refresh(new_user)
    return new_user

def get_user_by_id(user_id: int, db):
    user = db.query(User).filter(User.id == user_id).first()
    return user

def get_user_by_name(name: str, db):
    user = db.query(User).filter(User.name == name).first()
    return user

def get_all_users(db):
    users = db.query(User).all()
    return users

def update_user(user_id: int, user: User, db):
    user_to_update = db.query(User).filter(User.id == user_id).first()
    user_to_update.name = user.name
    user_to_update.email = user.email
    user_to_update.password = user.password
    user_to_update.phone = user.phone
    user_to_update.role_id = user.role_id
    db.commit()
    db.refresh(user_to_update)
    return user_to_update

def delete_user(user_id: int, db):
    user = db.query(User).filter(User.id == user_id).first()
    db.delete(user)
    db.commit()

def exist_user(name: str, db):
    user = db.query(User).filter(func.upper(User.name) == name.upper()).first()
    return user is not None