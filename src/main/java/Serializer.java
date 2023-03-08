import java.io.*;

public class Serializer<O>{

    public void serializeObject(O object, String filePath) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filePath);

        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(object);

        out.close();
        fileOut.close();
    }

    public O deserializeObject(String filePath) throws IOException, ClassNotFoundException{
        FileInputStream fileIn = new FileInputStream(filePath);

        ObjectInputStream in = new ObjectInputStream(fileIn);
        O readObject = (O) in.readObject();

        fileIn.close();
        in.close();

        return readObject;
    }

    public long getSerialVersionUID(O object){
        return ObjectStreamClass.lookup(object.getClass()).getSerialVersionUID();
    }

}
