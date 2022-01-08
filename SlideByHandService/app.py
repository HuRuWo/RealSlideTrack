import uvicorn as uvicorn
from fastapi import FastAPI
from fastapi.params import Depends
from sqlalchemy.orm import Session

from hand_slide_path.db import get_session, SlidePath

app = FastAPI()


@app.get("/")
async def root():
    return {"message": "hello world"}


@app.get("/slide_path")
async def add_slide(end_x, end_y, path_json, db: Session = Depends(get_session)):
    slide_path = SlidePath(end_x=end_x, end_y=end_y, path_json=path_json)
    try:
        db.add(slide_path)
        db.commit()
        li = db.query(SlidePath).all()
        for l in li:
            print(l.to_dict())
        return {"message": "写入成功"}
    except Exception as e:
        return {"message": "" + str(e) + ""}
    db.refresh(student)


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8001)
