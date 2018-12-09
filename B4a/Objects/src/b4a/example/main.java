package b4a.example;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.Serial.BluetoothAdmin _admin = null;
public static byte _nombrebt = (byte)0;
public anywheresoftware.b4a.objects.collections.List _listname = null;
public anywheresoftware.b4a.objects.collections.List _listmac = null;
public anywheresoftware.b4a.objects.Serial _serial1 = null;
public anywheresoftware.b4a.randomaccessfile.AsyncStreams _flux = null;
public static byte[] _donnee = null;
public static String _message = "";
public anywheresoftware.b4a.agraham.byteconverter.ByteConverter _bc = null;
public anywheresoftware.b4a.objects.ButtonWrapper _buttonsearch = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ble = null;
public anywheresoftware.b4a.objects.ButtonWrapper _avancer = null;
public anywheresoftware.b4a.objects.ButtonWrapper _reculer = null;
public anywheresoftware.b4a.objects.ButtonWrapper _droite = null;
public anywheresoftware.b4a.objects.ButtonWrapper _gauche = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _led = null;
public anywheresoftware.b4a.objects.ButtonWrapper _ledoff = null;
public anywheresoftware.b4a.objects.ButtonWrapper _ledon = null;
public anywheresoftware.b4a.objects.ButtonWrapper _buzzeronoff = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _buzzer = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _unoevo = null;
public b4a.example.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 52;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 54;BA.debugLine="Activity.LoadLayout(\"Layout1\")";
mostCurrent._activity.LoadLayout("Layout1",mostCurrent.activityBA);
 //BA.debugLineNum = 55;BA.debugLine="admin.Initialize(\"admin\")";
mostCurrent._admin.Initialize(processBA,"admin");
 //BA.debugLineNum = 56;BA.debugLine="serial1.Initialize(\"serial1\")";
mostCurrent._serial1.Initialize("serial1");
 //BA.debugLineNum = 57;BA.debugLine="LEDON.Visible=True";
mostCurrent._ledon.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 58;BA.debugLine="LEDOFF.Visible=False";
mostCurrent._ledoff.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 60;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 66;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 68;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 62;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public static String  _admin_devicefound(String _name,String _macaddress) throws Exception{
 //BA.debugLineNum = 69;BA.debugLine="Sub Admin_DeviceFound (Name As String, MacAddress";
 //BA.debugLineNum = 70;BA.debugLine="nombreBT=nombreBT+1";
_nombrebt = (byte) (_nombrebt+1);
 //BA.debugLineNum = 71;BA.debugLine="listName.Add(Name)";
mostCurrent._listname.Add((Object)(_name));
 //BA.debugLineNum = 72;BA.debugLine="listMac.Add(MacAddress)";
mostCurrent._listmac.Add((Object)(_macaddress));
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public static String  _admin_discoveryfinished() throws Exception{
int _choix = 0;
 //BA.debugLineNum = 76;BA.debugLine="Sub Admin_DiscoveryFinished";
 //BA.debugLineNum = 77;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 78;BA.debugLine="Msgbox(\"Nombre de périphériques trouvés \"&nombreB";
anywheresoftware.b4a.keywords.Common.Msgbox("Nombre de périphériques trouvés "+BA.NumberToString(_nombrebt),"Fin de la recherche",mostCurrent.activityBA);
 //BA.debugLineNum = 79;BA.debugLine="Dim choix As Int";
_choix = 0;
 //BA.debugLineNum = 80;BA.debugLine="choix=InputList(listName,\"Choisissez un périphéri";
_choix = anywheresoftware.b4a.keywords.Common.InputList(mostCurrent._listname,"Choisissez un périphérique",(int) (-1),mostCurrent.activityBA);
 //BA.debugLineNum = 81;BA.debugLine="ProgressDialogShow(\"Connexion au péripérique : \"";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Connexion au péripérique : "+BA.ObjectToString(mostCurrent._listname.Get(_choix)));
 //BA.debugLineNum = 82;BA.debugLine="serial1.Connect(listMac.Get(choix))";
mostCurrent._serial1.Connect(processBA,BA.ObjectToString(mostCurrent._listmac.Get(_choix)));
 //BA.debugLineNum = 83;BA.debugLine="End Sub";
return "";
}
public static String  _avancer_down() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub Avancer_Down";
 //BA.debugLineNum = 100;BA.debugLine="message=\"Z\"";
mostCurrent._message = "Z";
 //BA.debugLineNum = 101;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 102;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 103;BA.debugLine="End Sub";
return "";
}
public static String  _avancer_up() throws Exception{
 //BA.debugLineNum = 105;BA.debugLine="Sub Avancer_Up";
 //BA.debugLineNum = 106;BA.debugLine="message=\"B\"";
mostCurrent._message = "B";
 //BA.debugLineNum = 107;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 108;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 109;BA.debugLine="End Sub";
return "";
}
public static String  _buttonsearch_click() throws Exception{
 //BA.debugLineNum = 91;BA.debugLine="Sub ButtonSearch_Click";
 //BA.debugLineNum = 92;BA.debugLine="admin.StartDiscovery";
mostCurrent._admin.StartDiscovery();
 //BA.debugLineNum = 93;BA.debugLine="ProgressDialogShow(\"Recherche de périphériques Bl";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Recherche de périphériques Bluetooth...");
 //BA.debugLineNum = 94;BA.debugLine="listName.Initialize";
mostCurrent._listname.Initialize();
 //BA.debugLineNum = 95;BA.debugLine="listMac.Initialize";
mostCurrent._listmac.Initialize();
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _buzzeronoff_click() throws Exception{
 //BA.debugLineNum = 148;BA.debugLine="Sub BuzzerOnOff_Click";
 //BA.debugLineNum = 149;BA.debugLine="message=\"R\"";
mostCurrent._message = "R";
 //BA.debugLineNum = 150;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 151;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return "";
}
public static String  _droite_down() throws Exception{
 //BA.debugLineNum = 136;BA.debugLine="Sub Droite_Down";
 //BA.debugLineNum = 137;BA.debugLine="message=\"D\"";
mostCurrent._message = "D";
 //BA.debugLineNum = 138;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 139;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
return "";
}
public static String  _droite_up() throws Exception{
 //BA.debugLineNum = 142;BA.debugLine="Sub Droite_Up";
 //BA.debugLineNum = 143;BA.debugLine="message=\"B\"";
mostCurrent._message = "B";
 //BA.debugLineNum = 144;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 145;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 146;BA.debugLine="End Sub";
return "";
}
public static String  _gauche_down() throws Exception{
 //BA.debugLineNum = 124;BA.debugLine="Sub Gauche_Down";
 //BA.debugLineNum = 125;BA.debugLine="message=\"Q\"";
mostCurrent._message = "Q";
 //BA.debugLineNum = 126;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 127;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 128;BA.debugLine="End Sub";
return "";
}
public static String  _gauche_up() throws Exception{
 //BA.debugLineNum = 130;BA.debugLine="Sub Gauche_Up";
 //BA.debugLineNum = 131;BA.debugLine="message=\"B\"";
mostCurrent._message = "B";
 //BA.debugLineNum = 132;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 133;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Dim admin As BluetoothAdmin";
mostCurrent._admin = new anywheresoftware.b4a.objects.Serial.BluetoothAdmin();
 //BA.debugLineNum = 25;BA.debugLine="Dim nombreBT As Byte";
_nombrebt = (byte)0;
 //BA.debugLineNum = 26;BA.debugLine="Dim listName As List";
mostCurrent._listname = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 27;BA.debugLine="Dim listMac As List";
mostCurrent._listmac = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 28;BA.debugLine="Dim serial1 As Serial";
mostCurrent._serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 29;BA.debugLine="Dim Flux As AsyncStreams";
mostCurrent._flux = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 31;BA.debugLine="Dim Donnee() As Byte";
_donnee = new byte[(int) (0)];
;
 //BA.debugLineNum = 32;BA.debugLine="Dim message As String";
mostCurrent._message = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim bc As ByteConverter";
mostCurrent._bc = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 35;BA.debugLine="Private ButtonSearch As Button";
mostCurrent._buttonsearch = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private BLE As ImageView";
mostCurrent._ble = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private Avancer As Button";
mostCurrent._avancer = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private Reculer As Button";
mostCurrent._reculer = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private Droite As Button";
mostCurrent._droite = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private Gauche As Button";
mostCurrent._gauche = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private LED As ImageView";
mostCurrent._led = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private LEDOFF As Button";
mostCurrent._ledoff = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private LEDON As Button";
mostCurrent._ledon = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private BuzzerOnOff As Button";
mostCurrent._buzzeronoff = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private Buzzer As ImageView";
mostCurrent._buzzer = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private UNOEVO As ImageView";
mostCurrent._unoevo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static String  _ledoff_click() throws Exception{
 //BA.debugLineNum = 162;BA.debugLine="Sub LEDOFF_Click";
 //BA.debugLineNum = 163;BA.debugLine="message=\"F\"";
mostCurrent._message = "F";
 //BA.debugLineNum = 164;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 165;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 166;BA.debugLine="LEDON.Visible=True";
mostCurrent._ledon.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 167;BA.debugLine="LEDOFF.Visible=False";
mostCurrent._ledoff.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 168;BA.debugLine="End Sub";
return "";
}
public static String  _ledon_click() throws Exception{
 //BA.debugLineNum = 154;BA.debugLine="Sub LEDON_Click";
 //BA.debugLineNum = 155;BA.debugLine="message=\"L\"";
mostCurrent._message = "L";
 //BA.debugLineNum = 156;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 157;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 158;BA.debugLine="LEDOFF.Visible=True";
mostCurrent._ledoff.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 159;BA.debugLine="LEDON.Visible=False";
mostCurrent._ledon.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _reculer_down() throws Exception{
 //BA.debugLineNum = 112;BA.debugLine="Sub Reculer_Down";
 //BA.debugLineNum = 113;BA.debugLine="message=\"S\"";
mostCurrent._message = "S";
 //BA.debugLineNum = 114;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 115;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 116;BA.debugLine="End Sub";
return "";
}
public static String  _reculer_up() throws Exception{
 //BA.debugLineNum = 118;BA.debugLine="Sub Reculer_Up";
 //BA.debugLineNum = 119;BA.debugLine="message=\"B\"";
mostCurrent._message = "B";
 //BA.debugLineNum = 120;BA.debugLine="Donnee=bc.StringToBytes(message,\"ASCII\")";
_donnee = mostCurrent._bc.StringToBytes(mostCurrent._message,"ASCII");
 //BA.debugLineNum = 121;BA.debugLine="Flux.Write(Donnee)";
mostCurrent._flux.Write(_donnee);
 //BA.debugLineNum = 122;BA.debugLine="End Sub";
return "";
}
public static String  _serial1_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 85;BA.debugLine="Sub Serial1_Connected (Success As Boolean)";
 //BA.debugLineNum = 86;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 87;BA.debugLine="Msgbox(\"Connecté...\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Connecté...","",mostCurrent.activityBA);
 //BA.debugLineNum = 88;BA.debugLine="Flux.Initialize(serial1.InputStream,serial1.Outpu";
mostCurrent._flux.Initialize(processBA,mostCurrent._serial1.getInputStream(),mostCurrent._serial1.getOutputStream(),"Flux");
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
return "";
}
}
