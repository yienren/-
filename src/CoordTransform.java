/**
 * Created by Administrator on 15-1-14.
 */
public class CoordTransform {
    private double PI  = 3.1415926;

    public class oval{
        //大地坐标弧度B:大地坐标弧度L:中央子午线L0:" + L0

        public double getB() {
            return B;
        }
        public void setB(double B) {
            this.B = B;
        }
        public double getL() {
            return L;
        }
        public void setL(double L) {
            this.L = L;
        }

        public double getX() {
            return X;
        }

        public void setX(double X) {
            this.X = X;
        }

        public double getY() {
            return Y;
        }

        public void setY(double Y) {
            this.Y = Y;
        }

        public double getL0() {
            return L0;
        }

        public void setL0(double L0) {
            this.L0 = L0;
        }
        private  double B;
        private  double L;
        private  double X;
        private  double Y;
        private  double L0;
        //转换参数以从地方到WGS84为准
        //L0为WGS84坐标系的中央经线
        //L1为转到地方坐标系的中央经线
        //dx,dy 平面坐标到地方坐标的平移参数
        //dx1,dy1 为WGS84到54或80平面坐标的平移参数
    }
    private  oval BLtoXy(int el_Type, oval ov)
    {

        //函数功能：BL转换为84_XY
        //输入：B，L
        //输出：X,Y
        //B、L为大地坐标，L0为中央子午线，el_Type为椭球基准

        double B_SHORT_AXE = 0;  //短半轴
        double A_LONG_AXE = 0;   //长半轴
        double F = 0;            //扁率
        double E2;               //第一偏心率
        double E4;
        double E6;
        double E8;
        double E10;
        double A0;
        double A2;
        double A4;
        double A6;
        double A8;
        double M0;
        double t;
        double ETA2;
        double NU;
        double x0;

        PI = 3.14159265358979;

        switch (el_Type)
        {
            case 84:
                A_LONG_AXE = 6378137.0;
                F = 1 / 298.257223563;
                break;
            case 80:
                A_LONG_AXE = 6378140.0;
                F = 1 / 298.257;
                break;
            case 54:
                A_LONG_AXE = 6378245.0;
                F = 1 / 298.3;
                break;
        }

        //A_LONG_AXE为WGS_84椭球的长半轴，B_SHORT_AXE为WGS_84椭球的短半轴
        //F为椭球的扁率，E2为椭球的第一偏心率
        B_SHORT_AXE = A_LONG_AXE * (1 - F);
        //指定数字的指定次幂pow
        E2 = (Math.pow(A_LONG_AXE, 2) - Math.pow(B_SHORT_AXE, 2)) / Math.pow(A_LONG_AXE, 2);


        //求L与中央子午线L0的经差，并将B、L化为弧度
        ov.setL((ov.getL() - ov.getL0()) * PI / 180);
        ov.setB(ov.getB() * PI / 180);

        //E2、E4、E6、E8、E10为中间过程辅助计算量
        E4 = Math.pow(E2, 2);   //第一扁率平方
        E6 = E4 * E2;           //第一扁率3次方
        E8 = Math.pow(E4, 2);   //第一扁率4次方
        E10 = E2 * E8;          //第一扁率5次方

        //A0、A2、A4、A6、A8为中间过程辅助计算量   子午线计算公式系数赤道算起。

        A0 = 1 + 3 * E2 / 4 + 45 * E4 / 64 + 175 * E6 / 256 + 11025 * E8 / 16384 + 43659 * E10 / 65536;
        A2 = -(3 * E2 / 4 + 15 * E4 / 16 + 525 * E6 / 512 + 2205 * E8 / 2048 + 72765 * E10 / 65536) / 2;
        A4 = (15 * E4 / 64 + 105 * E6 / 256 + 2205 * E8 / 4096 + 10395 * E10 / 10384) / 4;
        A6 = -(35 * E6 / 512 + 315 * E8 / 2048 + 31185 * E10 / 13072) / 6;
        A8 = 315 * E8 / 16384 / 8;
        //计算辅助量M0、T 、NU、ETA2以及相应的子午线弧长X0

        M0 = ov.getL() * Math.cos(ov.getB());
        t = Math.tan(ov.getB());
        ETA2 = E2 * Math.pow((Math.cos(ov.getB())), 2) / (1 - E2);
        NU = A_LONG_AXE / Math.sqrt(1 - E2 * Math.pow((Math.sin(ov.getB())), 2));

        x0 = A_LONG_AXE * (1 - E2) * (A0 * ov.getB() + A2 * Math.sin(2 * ov.getB()) + A4 * Math.sin(4 * ov.getB()) + A6 * Math.sin(6 * ov.getB()) + A8 * Math.sin(8 * ov.getB()));
       // System.out.println("x0-----------"+x0);
        //计算X、Y
        ov.setX( x0 + NU * t * Math.pow(M0, 2) / 2 + NU * t * Math.pow(M0, 4) * (5 - Math.pow(t, 2) + 9 * ETA2 + 4 * Math.pow(ETA2, 2)) / 24 + (61 - 58 * Math.pow(t, 2) + Math.pow(t, 4) + 270 * ETA2 - 330 * ETA2 * Math.pow(t, 2)) * NU * t * Math.pow(M0, 6) / 720);
        ov.setY(NU * M0 + (1 - Math.pow(t, 2) + ETA2) * NU * Math.pow(M0, 3) / 6 + (5 - 18 * Math.pow(t, 2) + Math.pow(t, 4) - 14 * ETA2 - 58 * ETA2 * Math.pow(t, 2)) * NU * Math.pow(M0, 5) / 120 + 500000.0);
        return ov;
    }
    public class xy
    {

        public double getX() {
            return X;
        }

        public void setX(double X) {
            this.X = X;
        }

        public double getY() {
            return Y;
        }

        public void setY(double Y) {
            this.Y = Y;
        }
        private  double X;
        private  double Y;
    }

    class parameter{

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {
            this.angle = angle;
        }

        public double getC() {
            return c;
        }

        public void setC(double c) {
            this.c = c;
        }

        public double getD() {
            return d;
        }

        public void setD(double d) {
            this.d = d;
        }

        public double getPx() {
            return px;
        }

        public void setPx(double px) {
            this.px = px;
        }

        public double getPy() {
            return py;
        }

        public void setPy(double py) {
            this.py = py;
        }

        public double getK() {
            return k;
        }

        public void setK(double k) {
            this.k = k;
        }
        double angle;double c;double d; double px; double py; double k;

    }

    private  oval OffSet_Rotate_Coor(parameter pa,oval ov)
    {
        double x2;
        double y2;
        double x0;
        double y0;
        double x3;
        double y3;

        pa.setAngle(pa.getAngle()* PI / 180);
        x0 = ov.getX();
        y0 = ov.getY();
        //Anchor
        x2 = x0 - pa.getPx();
        y2 = y0 - pa.getPy();
        //Rotate
        if (pa.getAngle() != 0)
        {
            x3 = pa.getK() * (x2 * Math.cos(pa.getAngle()) + y2 * Math.sin(pa.getAngle()));
            y3 = pa.getK() * (y2 * Math.cos(pa.getAngle()) - x2 * Math.sin(pa.getAngle()));
        }
        else
        {
            x3 = x2;
            y3 = y2;
        }

        ov.setX(pa.getPx() + x3 + pa.getC());
        ov.setY(pa.getPy() + y3 + pa.getD());
         System.out.println("36" + ov.getY()+","+ov.getX());
        return ov;
    }

    public  String Convert_GuiYang(double x0,double y0)
    {
        //以下为标准的转换程序，勿修改其中参数
        //设置椭圆
        oval ov =new oval();
        ov.setB(y0);
        ov.setL(x0);
        ov.setL0(108);
        int el_Type = 84;

        BLtoXy(el_Type,ov);
        //设置参数
        parameter pa =new parameter();
        //弧度
        pa.setAngle(0.001072601111);
        //平移量x131
        pa.setC(162 - 24 + 3);
        //平移量y120
        pa.setD( -137 - 18 + 1);
        //扁率
        pa.setK(1.00017149);
        //短半轴
        pa.setPx(2935226.101);
        //长半轴
        pa.setPy(363678.648);

        OffSet_Rotate_Coor(pa, ov);
        return ("36" + ov.getY()+","+ov.getX());

    }
    public  String Convert_NanNing(double x0,double y0)
    {
        //以下为标准的转换程序，勿修改其中参数
        //设置椭圆
        oval ov =new oval();
        ov.setB(y0);
        ov.setL(x0);
        ov.setL0(108);
        int el_Type = 54;

        BLtoXy(el_Type,ov);
        //设置参数
        parameter pa =new parameter();
        //弧度
        pa.setAngle(0.001072601111);
        //25平移量y131
        pa.setC(162 - 24 + 3-400);
        //26平移量x120
        pa.setD( -137 - 18 + 1-997);
        //扁率
        pa.setK(1.00017149);
        //短半轴
        pa.setPx(2935226.101);
        //长半轴
        pa.setPy(363678.648);

        OffSet_Rotate_Coor(pa, ov);
        return ("36" + ov.getY()+","+ov.getX());

    }


    /// <summary>
    /// 1984经纬度转换为1954南宁地方坐标
    /// </summary>
    /// <param name="x0">1984坐标的纬度 格式为度</param>
    /// <param name="y0">1984坐标的经度 格式为度</param>
    /// <returns>返回带36度带的地方坐标,如:3623456.12,257869.456.如果x0,y0中存在小于0或为空,返回坐标为0,0</returns>
    public  String Convert_Fouction(double x0, double y0)
    {
        if (x0 > 0 && y0 > 0)
        {
            Convert_GuiYang(x0, y0);
        }
        else
        {
            x0 = 36500000;
            y0 = 2500000;
        }

        return x0+";"+y0;
    }
    public static void main(String[] args) {
        CoordTransform c = new CoordTransform();
        //  c.Convert_GuiYang(108.0,23.0);
        c.Convert_Fouction(108.0,23.0);
    }
}