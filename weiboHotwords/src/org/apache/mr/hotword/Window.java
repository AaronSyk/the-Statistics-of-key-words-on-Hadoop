package org.apache.mr.hotword;


import java.awt.*;  
import java.awt.event.*;  

import javax.swing.*;  

import java.util.*;  
import java.io.*;  

import javax.swing.table.*;  

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.util.*;  
import java.lang.Math;  
import java.net.URI;
import java.io.File; 

public class Window extends JFrame {
	
	public static final String  HDFS = "hdfs://172.16.42.10:9000";

	
	JFrame f_major = new JFrame("关键词词频统计");  
    JTabbedPane tp = new JTabbedPane();  

    Font ft = new Font("Serif", Font.TRUETYPE_FONT, 18);  
    Font ft1 = new Font("Serif", Font.ROMAN_BASELINE, 20);  
    Font ft2 = new Font("Serif", Font.ROMAN_BASELINE, 15);  
    Font ft3 = new Font("Serif", Font.TRUETYPE_FONT, 16);  


    JPanel panel = new JPanel();  

    //控件定义  
    JLabel keyword_l = new JLabel("关键词：",JLabel.RIGHT);
    static JTextField keyword_t = new JTextField();
    JLabel time_l = new JLabel("运行时间：",JLabel.RIGHT);
    JTextField time_t = new JTextField();
    
    JLabel words_l=new JLabel("-----分词数据-----", JLabel.CENTER);  
    JLabel result_l=new JLabel("-----统计结果-----", JLabel.CENTER);  
    
    JButton start_b1=new JButton("开始");  
    JButton showimage = new JButton("显示统计图");
    JTable jTable_1 =new JTable();
    JTable jTable_2 =new JTable();

    
    JScrollPane a2=new JScrollPane(jTable_2,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
    
    
    public static String keyword() {
    	return keyword_t.getText();
    }
   
   
    public static void main(String[] args) throws IOException {  
    	
    	Window win = new Window();  
    	win.go();  
    }  
    
    /**
     * @param args
     * @throws IOException
     */
    /**
     * @param args
     * @throws IOException
     */
    public void go() throws IOException {  
      f_major.setDefaultCloseOperation(3);
        //窗体界面设计  
      f_major.setBounds(250, 81, 900, 600);
      f_major.getContentPane().setLayout(new BorderLayout());  
      f_major.getContentPane().add("Center", tp);
    
      f_major.setFont(ft);  
      
      f_major.setResizable(false);  

      //选项卡界面设计  
      tp.add("分词统计", panel);  
      
      tp.setFont(ft);  

      
      
       //界面设计  
        panel.setLayout(null);  
      
        //结果显示区界面  
        result_l.setSize(200, 40);  
        result_l.setLocation(300, 10);  
        result_l.setFont(ft1); 
        
		
        
        keyword_l.setSize(80, 40);  
        keyword_l.setLocation(36, 450);  
        keyword_l.setFont(ft2);  
        
        keyword_t.setEditable(true);
        keyword_t.setHorizontalAlignment(JTextField.RIGHT);
        keyword_t.setSize(80, 40);
        keyword_t.setLocation(120, 450);  
        keyword_t.setFont(ft2);  
        
        
        time_l.setSize(80, 40);  
        time_l.setLocation(640, 450);  
        time_l.setFont(ft2);  
        
        time_t.setEditable(false);
        time_t.setSize(120, 40);
        time_t.setLocation(720, 450);  
        time_t.setFont(ft2);  
        time_t.setHorizontalAlignment(JTextField.RIGHT);
        

        //重置  
        start_b1.setSize(150, 40);  
        start_b1.setLocation(460, 450);  
          
        showimage.setSize(150,40);
        showimage.setLocation(300, 450);
      
        jTable_2.setSize(440, 350);  
        jTable_2.setLocation(200, 40); 
        
          
       
       
        a2.setViewportView(jTable_2);  
        a2.setSize(440, 350);  
        a2.setLocation(200, 60);  
           
        panel.add(keyword_l);
        panel.add(keyword_t);
        panel.add(time_l);
        panel.add(time_t);
        panel.add(words_l);  
        panel.add(a2);    
        panel.add(result_l);  
        panel.add(start_b1); 
        panel.add(showimage);
        
        f_major.setVisible(true);  
        start_b1.addActionListener(new start_b1AL());   
        showimage.addActionListener(new showimage_AL());
    } 
    
    
    
    class start_b1AL implements ActionListener{
    	    	
    	public void actionPerformed(ActionEvent e) {
    		Map<String,String>  path = new HashMap<String,String>();  
        	path.put("input", HDFS+"/file/weibo");
        	path.put("output", HDFS+"/file/weibo/output");
			// TODO Auto-generated method stub
    		
    		try {
    			long time =System.currentTimeMillis();
				JobWorker.run(path);
				time=System.currentTimeMillis()-time;
				String timeString="";
				timeString+=time;
				time_t.setText(timeString+"ms");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    		inHive.run(path); 
    		
    		 Map<String, String> map = new TreeMap<String, String>();
    	     map = inHive.getResult();
    		 
    	     
    		 String[] columnNames =  
    	            { "时间","关键词","频次" };
    		 Object[][] obj = new Object[13][3];  
    		 int posOfRow=0;
    		
    		 obj[0][0] = new String("20160221-1:00");
    		 obj[0][1] = keyword_t.getText();
    		 obj[0][2] = map.get("1");
    		 obj[1][0] = new String("20160221-2:00");
    		 obj[1][1] = Window.keyword();
    		 obj[1][2] = map.get("2");
    		 obj[2][0] = new String("20160221-3:00");
    		 obj[2][1] = keyword_t.getText();
    		 obj[2][2] = map.get("3");
    		 obj[3][0] = new String("20160221-4:00");
    		 obj[3][1] = keyword_t.getText();
    		 obj[3][2] = map.get("4");
    		 obj[4][0] = new String("20160221-5:00");
    		 obj[4][1] = keyword_t.getText();
    		 obj[4][2] = map.get("5");
    		 obj[5][0] = new String("20160221-6:00");
    		 obj[5][1] = keyword_t.getText();
    		 obj[5][2] = map.get("6");
    		 obj[6][0] = new String("20160221-7:00");
    		 obj[6][1] = keyword_t.getText();
    		 obj[6][2] = map.get("7");
    		 obj[7][0] = new String("20160221-8:00");
    		 obj[7][1] = keyword_t.getText();
    		 obj[7][2] = map.get("8");
    		 obj[8][0] = new String("20160221-9:00");
    		 obj[8][1] = keyword_t.getText();
    		 obj[8][2] = map.get("9");
    		 obj[9][0] = new String("20160221-10:00");
    		 obj[9][1] = keyword_t.getText();
    		 obj[9][2] = map.get("10");
    		 obj[10][0] = new String("20160221-11:00");
    		 obj[10][1] = keyword_t.getText();
    		 obj[10][2] = map.get("11");
    		 obj[11][0] = new String("20160221-12:00");
    		 obj[11][1] = keyword_t.getText();
    		 obj[11][2] = map.get("12");
    	
    	     jTable_2 = new JTable(obj, columnNames);  
    	       
    	     /* 
    	      * 设置JTable的列默认的宽度和高度 
    	     */  
    		
    	        TableColumn column = null;  
    	        int colunms =jTable_2.getColumnCount();
    	        
    	        for(int i = 0; i < colunms; i++)  
    	        {  
    	            column = jTable_2.getColumnModel().getColumn(i);  
    	            /*将每一列的默认宽度设置为100*/  
    		
    	            column.setPreferredWidth(182);  
    	        }  
    	        
    	        DefaultTableCellRenderer   r   =   new   DefaultTableCellRenderer();   
    	        r.setHorizontalAlignment(JLabel.CENTER);
    	        jTable_2.setDefaultRenderer(Object.class,   r);
    	        a2.getViewport().add(jTable_2);
    	        
		}
    }
    
    class showimage_AL implements ActionListener{
    	public void actionPerformed(ActionEvent e) {
    		LineCharts charts = new LineCharts("hot word");
    		charts.run(); 
    	}
    }
}
