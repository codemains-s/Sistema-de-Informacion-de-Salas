from pydantic import BaseModel

class Schedule(BaseModel):
    day : str
    start_time : str
    end_time : str

class ScheduleOut(Schedule):
   id : int