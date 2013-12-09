SpaceGroupVisualizer
====================

### Documentation Utilities

####QConvex

public static **QMesh** call(**PointList** points)

Receives a 'Pointlist' and returns the Convexhull as a 'QMesh'.
Uses qconvex for the calculations.

*Example Code*

        PointList p=new PointList();
        p.gen_randomPoints(8);
        System.out.println(p.toString());

        QMesh m1=QConvex.call(p);
        System.out.println(m1.getFaces());

####QDelaunay

public static **QMesh** call(**PointList** points)

Receive a 'Pointlist' and returns the Triangulated Mesh as a QMesh
Uses qdelaunay for the calculations.

*Example Code*

        PointList p=new PointList();
        p.gen_randomPoints(8);
        System.out.println(p.toString());

        QMesh m1=QDelaunay.call(p);
        System.out.println(m1.getFaces());

####QVoronoi

public static **QMesh** call(**PointList** points)

Receive a 'Pointlist' and returns the ConvexHull as a QMesh
Uses qvoronoi for the calculations.

*Example Code*

        PointList p=new PointList();
        p.gen_randomPoints(8);
        System.out.println(p.toString());

        QMesh m1=QVoronoi.call(p);
        System.out.println(m1.getFaces());