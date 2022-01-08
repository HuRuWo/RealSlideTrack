
from sqlalchemy import create_engine, Column, Integer, String, Boolean, FLOAT
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, relationship

SQLALCHEMY_DATABASE_URL = "sqlite:///./hand_slide_path/data.db"

engine = create_engine(
    SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False}
)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()


class SlidePath(Base):
    __tablename__ = "slide_path"
    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    end_x = Column(FLOAT)
    end_y = Column(FLOAT)
    path_json = Column(String)

    def to_dict(self):
        return {c.name: getattr(self, c.name) for c in self.__table__.columns}

Base.metadata.create_all(engine)


def get_session():
    db = SessionLocal()
    db.execute('pragma foreign_keys=on')
    try:
        yield db
    finally:
        db.close()
