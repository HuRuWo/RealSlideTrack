from sqlalchemy.orm import Session

from db import SessionLocal, SlidePath

db = SessionLocal()


def get_slide_path(end_x, scope=5, one=True):
    # db.execute('pragma foreign_keys=on')
    if one:
        li = db.query(SlidePath).filter(SlidePath.end_x.between(end_x - scope, end_x + scope)).first()
        if li:
            return li.to_dict().get('path_json')
        else:
            raise Exception('暂时没有该范围内的路径数据')
    else:
        li = db.query(SlidePath).filter(SlidePath.end_x.between(end_x - scope, end_x + scope)).all()
        if li:
            paths = []
            for l in li:
                paths.append(l.to_dict().get('path_json'))
            return paths
        else:
            raise Exception('暂时没有该范围内的路径数据')


if __name__ == '__main__':
    print(get_slide_path(end_x = 300.0, scope = 100, one=False))
