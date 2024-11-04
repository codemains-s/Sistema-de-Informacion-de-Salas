from pydantic import BaseModel

class Role(BaseModel):
    name: str
       
class RoleOut(Role):
    id: int