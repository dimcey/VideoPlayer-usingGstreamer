package Client;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.gstreamer.ClockTime;
import org.gstreamer.Format;
import org.gstreamer.Gst;
import org.gstreamer.SeekType;
import org.gstreamer.Segment;
import org.gstreamer.State;
import org.gstreamer.elements.PlayBin2;
import org.gstreamer.swing.VideoComponent;

import GUI.GUI;

public class Pipeline {
	
	static PlayBin2 playbin;
	VideoComponent videoComponent;
	GUI myGui;
	
	//Pipeline constructor, initializing the playbin and the video Component
	//adding Video sink component to the playbin for displaying the video
	public Pipeline(){
		 playbin = new PlayBin2("AVPlayer");
		 videoComponent = new VideoComponent();
         playbin.setVideoSink(videoComponent.getElement());
	}
	
	public VideoComponent getVideoComponent(){
		return videoComponent;
	}

	public void changeState(State state) {
		playbin.setState(state);
	}

	public int getVolumePercen() {
		int percent = playbin.getVolumePercent();
		return percent;
	}

	public void setVolumePercen(int percent, String state) {
		if (state=="up"){
			playbin.setVolumePercent(percent + 15);			
		}
		if (state=="down"){
			playbin.setVolumePercent(percent - 15);			
		}
		if (state=="mute"){
			playbin.setVolumePercent(percent);
		}
	}

	public long getQueryPosition(TimeUnit _scaleUnit) {
		long position = playbin.queryPosition(_scaleUnit);
		return position;
	}

	public long getQueryDuration(TimeUnit _scaleUnit) {
		long duration = playbin.queryDuration(_scaleUnit);
		return duration;
	}

	public ClockTime getTotalQueryPosition() {
		ClockTime c = playbin.queryPosition();
		return c;
	}

	public ClockTime getTotalQueryDuration() {
		ClockTime c = playbin.queryDuration();
		return c;
	}

	public Segment getQuerySegment() {
		Segment s = playbin.querySegment();
		return s;
	}

	public void setShortSeek(long position, TimeUnit _scaleUnit) {
		playbin.seek((long) (position), _scaleUnit);
		
	}

	public void setLongSeek(long position, long d, double tmp) {
		playbin.seek(tmp, Format.TIME, 1, SeekType.CUR, position, SeekType.CUR, d);
	}

	//passing the url of the video to the playbin
	public void setInputFile(File f1) {
		playbin.setInputFile(f1);
	}

	public void setNewSeek(long duration, double perc, TimeUnit _scaleUnit) {
		playbin.seek((long) (duration * perc), _scaleUnit);
	}
	
}
