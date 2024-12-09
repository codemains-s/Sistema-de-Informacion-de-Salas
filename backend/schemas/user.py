from pydantic import BaseModel
from datetime import date


class User(BaseModel):
    program_id: int = 0
    name: str
    email: str
    phone: str


class UserLogin(BaseModel):
    email: str
    password: str


class UserCreate(User):
    password: str


class UserUpdate(User):
    role_id: int
    token: str
    program_id: int


class UserEdit(User):
    role_id: int
    program_id: int


class UserOut(User):
    id: int
    token: str
    program: str
    role: str
