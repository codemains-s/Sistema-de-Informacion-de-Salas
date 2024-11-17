from schemas.user import UserCreate, UserOut, UserEdit, UserUpdate
from models.tables import User
from controllers.roleController import get_role_by_name
from sqlalchemy import func, text
from passlib.context import CryptContext
from fastapi import HTTPException, status, Depends
from pydantic import EmailStr
from typing import List, Optional
from datetime import datetime, timedelta, timezone
from dotenv import load_dotenv
import os
from models.tables import Role
from models.tables import Program

load_dotenv(".env")

import jwt
from jwt import encode, decode, ExpiredSignatureError, InvalidTokenError
from functools import wraps

password_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


def create_user(new_user: UserOut, code: str, db):
    hashed_password = password_context.hash(new_user.password)
    usr = User(**new_user.dict(exclude={"password"}), password=hashed_password)
    secret_key = os.getenv("SECRET_KEY")
    algorithm = os.getenv("ALGORITHM")
    role_id = asign_role(code, db)
    usr.role_id = role_id
    payload = {"id": usr.id, "email": usr.email, "role_id": usr.role_id}
    token = encode(payload, secret_key, algorithm)
    usr.token = token
    db.add(usr)
    db.commit()
    db.refresh(usr)
    return usr.token


def validate_code(code: str):
    if code != os.getenv("SPECIAL_ADMIN_CODE") or code == "":
        return False
    return True


def asign_role(code: str, db):
    role_id = 0
    if validate_code(code):
        role_id = get_role_by_name("Coordinator", db)
    else:
        role_id = get_role_by_name("Monitor", db)
    return role_id


def validate_token(token: str) -> dict:
    secret_key = os.getenv("SECRET_KEY")
    algorithm = os.getenv("ALGORITHM")
    try:
        dato: dict = decode(token, secret_key, algorithms=[algorithm])
        return dato
    except ExpiredSignatureError:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED, detail="El token ha expirado"
        )
    except InvalidTokenError:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED, detail="Token no válido"
        )


def exist_token(email: str, password: str, db):
    user = (
        db.query(User).filter(User.email == email and User.password == password).first()
    )
    if not user:
        return False
    return user.token


def email_validation(email: str):
    try:
        email: EmailStr._validate(email)
    except ValueError:
        raise HTTPException(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            detail="Correo electrónico no válido",
        )
    return True


def validate_password(password: str):
    if not any(char.isupper() for char in password):
        raise HTTPException(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            detail="La contraseña debe tener al menos una letra mayúscula",
        )
    if not any(char.islower() for char in password):
        raise HTTPException(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            detail="La contraseña debe tener al menos una letra minúscula",
        )
    if not any(char.isdigit() for char in password):
        raise HTTPException(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            detail="La contraseña debe tener al menos un número",
        )
    if not any(char in ["$", "@", "#", "%", "!", "?"] for char in password):
        raise HTTPException(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            detail="La contraseña debe tener al menos un caracter especial",
        )
    if len(password) < 8:
        raise HTTPException(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            detail="La contraseña debe tener al menos 8 caracteres",
        )
    return True


def exist_user(email: str, db):
    user = db.query(User).filter(User.email == email).first()
    if not user:
        return False
    return user


def exist_user_id(user_id: int, db):
    user = db.query(User).filter(User.id == user_id).first()
    if not user:
        return False
    return user


def get_user_by_id(user_id: int, db):
    user = db.query(User).filter(User.id == user_id).first()
    role = db.query(Role).filter(Role.id == user.role_id).first()
    program = db.query(Program).filter(Program.id == user.program_id).first()

    if user is None:
        return None, None, None

    return user, role, program


def get_token_by_id(user_id: int, db):
    user = db.query(User).filter(User.id == user_id).first()
    return user.token


def get_all_users(db):
    users = db.query(User).all()
    return users


def update_user(user_id: int, update_user: UserUpdate, db) -> Optional[UserOut]:
    usr = db.query(User).filter(User.id == user_id).first()
    secret_key = os.getenv("SECRET_KEY")
    algorithm = os.getenv("ALGORITHM")
    if usr:
        payload = {
            "id": user_id,
            "email": update_user.email,
            "role_id": update_user.role_id,
        }
        usr.program_id = update_user.program_id
        usr.role_id = update_user.role_id
        usr.name = update_user.name
        usr.email = update_user.email
        usr.birthdate = update_user.birthdate
        usr.phone = update_user.phone
        usr.token = jwt.encode(payload, secret_key, algorithm)
        db.commit()
        db.refresh(usr)
        return usr


def exist_email(email: str, db):
    user = db.query(User).filter(User.email == email).first()
    if not user:
        return False
    return user


def delete_user(user_id: int, db):
    user = db.query(User).filter(User.id == user_id).first()
    if not user:
        print("Usuario no encontrado en la base de datos")
        return None

    db.delete(user)
    db.commit()

    # Verifica si quedan usuarios
    user_count = db.query(User).count()
    if user_count == 0:
        # Si no hay usuarios, reinicia el AUTO_INCREMENT a 1
        db.execute(text("ALTER TABLE users AUTO_INCREMENT = 1"))
    else:
        # Si hay usuarios, configura el siguiente ID como max_id + 1 (opcional en MySQL)
        max_id = db.query(func.max(User.id)).scalar()
        db.execute(text(f"ALTER TABLE users AUTO_INCREMENT = {max_id + 1}"))

    db.commit()
    return user
