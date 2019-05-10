package fileScan;

import java.io.*;
import java.nio.file.Path;

public class FileReader {

    public BufferedReader bufferedReader = null;


    FileInputStream file = null;
   // FileChannel ch = null;

    public void openFile(Path path) throws IOException{

        //File file = new File(path.toString());
        try {
            file = new FileInputStream(path.toString());
            bufferedReader = new BufferedReader(new InputStreamReader(file));
             //ch = file.getChannel();

        }catch ( FileNotFoundException  e){
            e.printStackTrace();
        }


    }

    public String getNextTuple(){

        String content = null;
            //String tuple = new Tuple(outputSchema, IDField.newRandomID(), new TextField(content));
        try{
            content = bufferedReader.readLine();


        }catch (IOException e){
            e.printStackTrace();
        } finally {

        }

        return content;

    }

    public void close(){
        try{
            bufferedReader.close();
            if (file != null) {
                file.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
