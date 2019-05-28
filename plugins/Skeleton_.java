package color;


import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;


/**
* Skeleton Filter
* 
* @author Xavier Philippeau (from work of Dr. Chai Quek)
*
*/
public class Skeleton_ implements PlugInFilter {

	// neighbours order
	private int[] dx = new int[] {-1, 0, 1,1,1,0,-1,-1};
	private int[] dy = new int[] {-1,-1,-1,0,1,1, 1, 0};

	// Smoothing pattern
	private int[] pattern1={-1,1,0,1,0,0,0,0};
	private int[] pattern2={0,1,0,1,-1,0,0,0};
	private int[] pattern3={0,0,-1,1,0,1,0,0};
	private int[] pattern4={0,0,0,1,0,1,-1,0};
	private int[] pattern5={0,0,0,0,-1,1,0,1};
	private int[] pattern6={-1,0,0,0,0,1,0,1};
	private int[] pattern7={0,1,0,0,0,0,-1,1};
	private int[] pattern8={0,1,-1,0,0,0,0,1};

	// filter configuration
	private boolean blackbackground = true;
	private int threshold = 0;

	//	 About...
	private void showAbout() {
		IJ.showMessage("Skeleton...","Skeleton Filter by Pseudocode");
	}

	public int setup(String arg, ImagePlus imp) {

		// about...
		if (arg.equals("about")) {
			showAbout(); 
			return DONE;
		}

		// else...
		if (imp==null) return DONE;

		// Configuration dialog.
		GenericDialog gd = new GenericDialog("Parameters");
		gd.addChoice("Background color", new String[]{"Black","White"}, "Black");
		gd.addNumericField("Black/White threshold",128,0);

		while(true) {
			gd.showDialog();
			if ( gd.wasCanceled() )	return DONE;

			this.blackbackground  =  gd.getNextChoice().equals("Black")?true:false;
			this.threshold  =  (int)gd.getNextNumber();

			if (this.threshold<=0) continue;
			if (this.threshold>=255) continue;
			break;
		}
		gd.dispose();

		return PlugInFilter.DOES_8G;
	}

	public void run(ImageProcessor ip) {

		// ImageProcessor -> ByteProcessor conversion
		ByteProcessor bp = new ByteProcessor(ip.getWidth(),ip.getHeight());
		for (int y = 0; y < ip.getHeight(); y++) {
			for (int x = 0; x < ip.getWidth(); x++) {
				bp.set(x,y,ip.getPixel(x,y));
			}
		}

		// filter
		ByteProcessor newbp = filter( bp );

		// ByteProcessor -> ImageProcessor conversion
		ImageProcessor out = new ByteProcessor(ip.getWidth(),ip.getHeight());
		for (int y = 0; y < ip.getHeight(); y++) {
			for (int x = 0; x < ip.getWidth(); x++) {
				out.set(x,y,newbp.get(x,y));
			}
		}
		ImagePlus newImg = new ImagePlus("Skeleton", out);
		newImg.show();

	}

	// -------------------------------------------------------------------------

	// Binary value of the gray scale image (0..255) -> (0,1)
	private int ChannelValue(ByteProcessor c,int x,int y) {
		if (c.getPixel(x,y)<this.threshold) return 0;
		return 1;
	}

	// Neighbourhood
	private int neighbourhood(ByteProcessor c,int x,int y) {
		int neighbourhood=0;

		for(int i=0;i<8;i++)
			neighbourhood += ChannelValue(c,x+dx[i],y+dy[i]);

		return neighbourhood;
	}

	// Transitions Count
	private int transitions(ByteProcessor c,int x,int y) {
		int Trans=0;

		for(int i=0;i<8;i++) {
			int pc = ChannelValue(c,x+dx[i],y+dy[i]);
			int pn = ChannelValue(c,x+dx[(i+1)%8],y+dy[(i+1)%8]);
			if ((pc==0) && (pn==1)) Trans++;
		}

		return Trans;
	}

	// Match a pattern
	private boolean matchPattern(ByteProcessor c,int x,int y,int[] pattern) {
		for(int i=0;i<8;i++) {
			if (pattern[i]==-1) continue;
			int v = ChannelValue(c,x+dx[i],y+dy[i]);
			if (pattern[i]!=v) return false;
		}
		return true;
	}

   // Match one of the 8 patterns
	private boolean matchOneOfPatterns(ByteProcessor c,int x,int y) {
		if (matchPattern(c,x,y,pattern1)) return true;
		if (matchPattern(c,x,y,pattern2)) return true;
		if (matchPattern(c,x,y,pattern3)) return true;
		if (matchPattern(c,x,y,pattern4)) return true;
		if (matchPattern(c,x,y,pattern5)) return true;
		if (matchPattern(c,x,y,pattern6)) return true;
		if (matchPattern(c,x,y,pattern7)) return true;
		if (matchPattern(c,x,y,pattern8)) return true;
		return false;
	}

	private ByteProcessor thinning(ByteProcessor original) {
		int width = original.getWidth();
		int height = original.getHeight();

		// previous image buffer
		ByteProcessor c = new ByteProcessor(width,height);
		for(int y=0;y<height;y++)
			for(int x=0;x<width;x++)
				if (this.blackbackground)
					c.set(x,y,255*ChannelValue(original,x,y));
				else
					c.set(x,y,255-255*ChannelValue(original,x,y));

		// new image buffer
		ByteProcessor c2  = new ByteProcessor(width,height);

		// loop until idempotence
		for(int loop=0;;loop++) {

			int pixelchangecount=0;

			// copy previous image in new image
			for(int y=0;y<height;y++)
				for(int x=0;x<width;x++) 
					c2.set(x,y,c.get(x,y));

			// for each pixel
			for(int y=1;y<height-1;y++) {

				for(int x=1;x<width-1;x++) {

					// pixel value
					int v = ChannelValue(c,x,y);

					// pixel not set -> next
					if (v==0) continue;

					// is a boundary ?
					int previousNeighbourhood = neighbourhood(c,x,y);
					if (previousNeighbourhood==8) continue;

					// is an extremity ?
					int currentNeighbourhood = neighbourhood(c2,x,y);
					if (currentNeighbourhood<=1) continue;
					if (currentNeighbourhood>=6) continue;

					// is a connection ?
					int transitionsCount = transitions(c2,x,y);

					// Addition to the original algorithm:
					// Preservation of the "Y curve" near the edges 
					if (transitionsCount==1 && previousNeighbourhood<=3) continue;

					if (transitionsCount==1) {
						pixelchangecount++;
						c2.set(x,y,0);
						continue;
					}

					// is a deletable pixel ?
					boolean matchOne = matchOneOfPatterns(c2,x,y);
					if (matchOne) {
						pixelchangecount++;
						c2.set(x,y,0);
						continue;
					}
				}
			}

			// no change -> return result
			if (pixelchangecount==0) return c;

			// swap image buffers, then loop.
			ByteProcessor tmp = c;
			c=c2;
			c2=tmp;
		}
	}

	// Return the skeleton
	public ByteProcessor filter(ByteProcessor bp) {
		ByteProcessor skel = thinning(bp);
		return skel;
	}
}
