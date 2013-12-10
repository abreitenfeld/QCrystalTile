SpaceGroupVisualizer
====================

### Documentation Utilities

####QConvex

public static **QMesh** call(**PointList** points, **String[]** args)

Receives a 'Pointlist' and returns the Convexhull as a 'QMesh'.
Uses qconvex for the calculations. 
Paramters for QConvex can be found [here](http://www.qhull.org/html/qconvex.htm)
If have no parameter u simply set it to a space.

*Example Code*

        PointList p=new PointList();
        p.gen_randomPoints(8);
        System.out.println(p.toString());

        String[] qargs={" "};
        QMesh m1=QConvex.call(p,qargs);
        System.out.println(m1.getFaces());

####QDelaunay

public static **QMesh** call(**PointList** points, **String[]** args)

Receive a 'Pointlist' and returns the Triangulated Mesh as a QMesh
Uses qdelaunay for the calculations.

Paramters for QDelaunay can be found [here](http://www.qhull.org/html/qdelaun.htm)
If have no parameter u simply set it to a space.

*Example Code*

        PointList p=new PointList();
        p.gen_randomPoints(8);
        System.out.println(p.toString());

        String[] qargs={" "};
        QMesh m1=QDelaunay.call(p,qargs);
        System.out.println(m1.getFaces());

####QVoronoi

public static **QMesh** call(**PointList** points, **String[]** args)

Receive a 'Pointlist' and returns the ConvexHull as a QMesh
Uses qvoronoi for the calculations.

Paramters for QVoronoi can be found [here](http://www.qhull.org/html/qvoronoi.htm)
If have no parameter u simply set it to a space.

*Example Code*

        PointList p=new PointList();
        p.gen_randomPoints(8);
        System.out.println(p.toString());

        String[] qargs={" "};
        QMesh m1=QVoronoi.call(p,qargs);
        System.out.println(m1.getFaces());