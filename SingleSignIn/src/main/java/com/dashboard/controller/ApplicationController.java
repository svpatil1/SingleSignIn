/**
 * 
 */
package com.dashboard.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dashboard.command.applicationCommand;
import com.dashboard.domain.Application;
import com.dashboard.service.ApplicationService;
import com.singlesignin.sqlscript.runSqlScript;

/**
 * @author Sanika Patil
 * This controller maps the requests related to upload functionality for ISV applications
 */
@RestController
public class ApplicationController {
	@Autowired
	private ApplicationService applicationService;
	
	@RequestMapping(value = "/instructions", method = RequestMethod.GET)
	public ModelAndView upload() {
		ModelAndView mav = new ModelAndView("/instructions");
		return mav;
	} 
	
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public ModelAndView about() {
		ModelAndView mav = new ModelAndView("/about");
		return mav;
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public ModelAndView upload(Model m) {
		m.addAttribute("command", new applicationCommand());
		ModelAndView mav = new ModelAndView("/upload");
		return mav;
	}
	
	/*
	 * uploadFiles() method is called when users(ISV and ADMIN) are uploading the file on server.
	 * It firstly stores the file temporarily.
	 */
	
	@RequestMapping(value = "/uploadSuccessful", method = RequestMethod.GET)
	public ModelAndView uploadSucess(Model m) {
		ModelAndView mav = new ModelAndView("/uploadSuccessful");
		return mav;
	}
	
	@RequestMapping(value = "/uploadFailed", method = RequestMethod.GET)
	public ModelAndView uploadFail(Model m) {
		ModelAndView mav = new ModelAndView("/uploadFailed");
		return mav;
	}
	
	@RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
	public ModelAndView uploadFiles(@ModelAttribute("command") applicationCommand cmd, Model m,  HttpSession session, @RequestParam("file") MultipartFile[] files) throws IOException, SQLException
	{	
		if(cmd.getAppDescription().isEmpty()) {
			m.addAttribute("err", "Application Description or Application name cannot be empty"); // it takes care that the app description is not empty
			ModelAndView mav = new ModelAndView("/upload");
			return mav;
			
		}
		else if(cmd.getName().isEmpty()) {
			m.addAttribute("err", "Application Description or Application name cannot be empty"); // it takes care that the app description is not empty
			ModelAndView mav = new ModelAndView("/upload");
			return mav;
			
		}				
		else {
				if((files[0].getOriginalFilename().endsWith(".war")) && (files[1].getOriginalFilename().endsWith(".sql") && (files[2].getOriginalFilename().endsWith(".png")))) {
							
						for (int i = 0; i < files.length; i++) {
							MultipartFile file = files[i];
							System.out.println("Original file name: "+ file.getOriginalFilename());
							System.out.println(file);
				
								try {
									
									byte[] bytes = file.getBytes();
					
									// Creating the root path to store war files and sql scripts
									String rootPath =System.getProperty("catalina.base");
									System.out.println(rootPath);
									
									File dirSql = new File("/tmp"); // directory to store sql script
									File dirWar = new File(rootPath + File.separator + "webapps"); // directory to store war files
									
									System.out.println("Sql directory"+dirSql);
									System.out.println("War directory"+dirWar);
									
					
									// If the uploaded file is an sql script, store in directory created for sql script
									if((file.getOriginalFilename().endsWith(".sql"))){
											
											File TempServerFile = new File(dirSql.getAbsolutePath()
													+ File.separator + file.getOriginalFilename());
											System.out.println("SQL Temp server file: "+ TempServerFile );
											String scriptPathSql = TempServerFile.toString();
											
											BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(TempServerFile));
											bos.write(bytes);
											bos.close();
											
											/*
											 * If the file is an sql script then run and delete it from the directory to save memory.
											 */
											
											runSqlScript test = new runSqlScript();
											try {
													test.run(scriptPathSql);
											}catch (Exception e){
												m.addAttribute("err", "SQL script failed to run"); // it takes care that the app description is not empty
												ModelAndView mav = new ModelAndView("/upload");
												return mav;
												}
											TempServerFile.delete();
									}
									/*
									 * If the uploaded file png i.e icon image, then store it in different path as mentioned
									 */
									else if((file.getOriginalFilename().endsWith(".png"))){
										File TempServerFileWar = new File(dirWar+ File.separator+ "SingleSignIn/resources/img" 
												+ File.separator + file.getOriginalFilename());
										System.out.println("War Temp server file: "+ TempServerFileWar );						
										BufferedOutputStream bosWar = new BufferedOutputStream(new FileOutputStream(TempServerFileWar));
										bosWar.write(bytes);
										bosWar.close();	
										
									}
									else {
										// If the uploaded file is an war file, store in directory created for war files
											File TempServerFileWar = new File(dirWar
													+ File.separator + file.getOriginalFilename());
											System.out.println("War Temp server file: "+ TempServerFileWar );						
											BufferedOutputStream bosWar = new BufferedOutputStream(new FileOutputStream(TempServerFileWar));
											bosWar.write(bytes);
											bosWar.close();	
											
											//Registering a new application in database, after uploading
											Application a = new Application();
											try {
													a.setName(cmd.getName());
													a.setUserId((Integer) session.getAttribute("userId"));
													a.setAppDesc(cmd.getAppDescription());
													
													String iconName = cmd.getName() + ".png";
													String appName = cmd.getName();
													String iconPath = "resources/img/" + iconName;
													a.setImageLocation(iconPath);
													System.out.println(a.toString());
													applicationService.registerApplication(a);
											}catch (Exception e){
												m.addAttribute("err", "The Application with same name already exist"); // it takes care that the app description is not empty
												ModelAndView mav = new ModelAndView("/upload");
												return mav;
											}
									}	
								}
							
							catch (Exception e) {
								ModelAndView mav = new ModelAndView("redirect:uploadFailed");
								return mav;
							}
						}
					
				}else {
							m.addAttribute("err", "Select correct files.");
							ModelAndView mav = new ModelAndView("/upload");
							return mav;
						  }
				ModelAndView mav = new ModelAndView("redirect:uploadSuccessful");
				return mav;
			
			}
		}
}
