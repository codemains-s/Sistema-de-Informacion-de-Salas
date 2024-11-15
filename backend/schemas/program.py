from pydantic import BaseModel

class Program(BaseModel):
    name : str
    description : str

class ProgramsOut(Program):
    id : int