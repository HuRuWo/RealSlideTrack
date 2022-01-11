from sqlalchemy.orm import Session

from db import SessionLocal, SlidePath
import numpy as np
import matplotlib.pyplot as plt

db = SessionLocal()


def show_data():
    x_list = []
    y_list = []

    for i in range(0, 1000, 100):
        x = i+1
        y = i + 100
        print(x,y)
        x_list.append(f'{x}-{y}')
        y_list.append(db.query(SlidePath).filter(SlidePath.end_x.between(x, y)).count())

    fig, ax = plt.subplots(figsize=(10, 7))
    ax.bar(
        x=x_list,  # Matplotlib自动将非数值变量转化为x轴坐标
        height=y_list,  # 柱子高度，y轴坐标
        width=0.6,  # 柱子宽度，默认0.8，两根柱子中心的距离默认为1.0
        align="center",  # 柱子的对齐方式，'center' or 'edge'
        color="grey",  # 柱子颜色
        edgecolor="red",  # 柱子边框的颜色
        linewidth=1.0  # 柱子边框线的大小
    )
    ax.set_title("path data", fontsize=15)

    xticks = ax.get_xticks()
    for i in range(len(y_list)):
        xy = (xticks[i], y_list[i] * 1.03)
        s = str(y_list[i])
        ax.annotate(
            text=s,  # 要添加的文本
            xy=xy,  # 将文本添加到哪个位置
            fontsize=12,  # 标签大小
        )

    plt.show()


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
    # print(get_slide_path(end_x = 300.0, scope = 100, one=False))
    show_data()
