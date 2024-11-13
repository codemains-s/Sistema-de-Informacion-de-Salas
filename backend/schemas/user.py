from pydantic import BaseModel
from datetime import date

class User(BaseModel):
    name : str
    email : str
    birthdate : date
    phone : str

class UserLogin(BaseModel):
    email : str
    password : str

class UserCreate(User):
    password : str

class UserUpdate(User):
    role_id : int

class UserEdit(User):
    role_id : int
    token : str


class UserOut(User):
    id: int
    token : str
    role_id: int
    
    