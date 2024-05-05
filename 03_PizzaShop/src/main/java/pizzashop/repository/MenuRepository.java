package pizzashop.repository;

import pizzashop.exceptions.MenuException;
import pizzashop.model.MenuDataModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MenuRepository {
    private static String filename;
    private List<MenuDataModel> listMenu;

    public MenuRepository(String filename){
        this.filename = filename;
    }

    private void readMenu() throws MenuException {
        //ClassLoader classLoader = MenuRepository.class.getClassLoader();
        File file = new File(filename);
        this.listMenu= new ArrayList();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while((line=br.readLine())!=null){
                MenuDataModel menuItem=getMenuItem(line);
                listMenu.add(menuItem);
            }
            br.close();
        } catch (FileNotFoundException e) {
            throw new MenuException("Could not find the menu file.", e);
        } catch (IOException e) {
            throw new MenuException("An error occurred while reading the menu.", e);
        }
    }

    private MenuDataModel getMenuItem(String line){
        MenuDataModel item=null;
        if (line==null|| line.equals("")) return null;
        StringTokenizer st=new StringTokenizer(line, ",");
        String name= st.nextToken();
        double price = Double.parseDouble(st.nextToken());
        item = new MenuDataModel(name, 0, price);
        return item;
    }

    public List<MenuDataModel> getMenu() throws MenuException {
        readMenu();//create a new menu for each table, on request
        return listMenu;
    }
}
