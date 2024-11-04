from pydantic import BaseModel

class Room(BaseModel):
    name: str
    address: str
    description: str
    capacity: int
    status: str
    image: str
    
    
    

class SalaOut(Room):
   id : int