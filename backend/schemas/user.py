from pydantic import BaseModel

class User(BaseModel):
    name: str
    email: str
    password: str
    phone: str
    role_id : int

class UserOut(User):
    id: int
    
    