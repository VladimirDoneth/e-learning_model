package dataBaseCore;

import java.io.*;

public class BinaryObjectConverter {
    public static int[] byteToArr1DI(byte [] bArr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bArr);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        return (int[]) objectInputStream.readObject();
    }

    public static int[][] byteToArr2DI(byte [] bArr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bArr);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        return (int[][]) objectInputStream.readObject();
    }

    public static double[][] byteToArr2DD(byte [] bArr)  throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bArr);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        return (double[][]) objectInputStream.readObject();
    }

    public static byte [] arr1DIToByte(int []arr) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream  objectOutputStream = new
                ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(arr);
        return byteOutputStream.toByteArray();
    }

    public static byte [] arr2DIToByte(int [][] arr) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream  objectOutputStream = new
                ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(arr);
        return byteOutputStream.toByteArray();
    }

    public static byte [] arr2DDToByte(double [][] arr) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream  objectOutputStream = new
                ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(arr);
        return byteOutputStream.toByteArray();
    }
}
