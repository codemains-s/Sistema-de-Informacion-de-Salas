from fastapi import FastAPI
from config.db import Base, engine
from routes import userRoutes, roleRoutes, roomRoutes, scheduleRoutes, roomScheduleRoutes, userRoomsRoutes
from fastapi.middleware.cors import CORSMiddleware

Base.metadata.create_all(bind=engine)

app = FastAPI(title="SIS", description="Sistema de asignación de salas a monitores", version="1.0.0")

origins = ['*']

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(roleRoutes.router, tags=["Role"])
app.include_router(userRoutes.router, tags=["User"])
app.include_router(roomRoutes.router, tags=["Room"])
app.include_router(scheduleRoutes.router, tags=["Schedule"])
app.include_router(roomScheduleRoutes.router, tags=["RoomSchedule"])
app.include_router(userRoomsRoutes.router, tags=["UserRooms"])


@app.get("/", tags=["Main"])
def main():
    return {"message": "Welcome to SIS"}