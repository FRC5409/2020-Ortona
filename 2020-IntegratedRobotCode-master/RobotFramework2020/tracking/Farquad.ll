//
// Inputs
//
Inputs
{
	Mat source0;
}

//
// Variables
//
Outputs
{
	Mat hsvThresholdOutput;
	ContoursReport findContoursOutput;
	ContoursReport convexHullsOutput;
}

//
// Steps
//

Step HSV_Threshold0
{
    Mat hsvThresholdInput = source0;
    List hsvThresholdHue = [0.0, 180.0];
    List hsvThresholdSaturation = [0.0, 255.0];
    List hsvThresholdValue = [73.38129496402878, 248.4726962457338];

    hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);
}

Step Find_Contours0
{
    Mat findContoursInput = hsvThresholdOutput;
    Boolean findContoursExternalOnly = false;

    findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);
}

Step Convex_Hulls0
{
    ContoursReport convexHullsContours = findContoursOutput;

    convexHulls(convexHullsContours, convexHullsOutput);
}

Step NTPublish_ContoursReport0
{
    ContoursReport ntpublishContoursreportData = convexHullsOutput;
    String ntpublishContoursreportName = "myContoursReport";
    Boolean ntpublishContoursreportPublishArea = true;
    Boolean ntpublishContoursreportPublishCenterx = true;
    Boolean ntpublishContoursreportPublishCentery = true;
    Boolean ntpublishContoursreportPublishWidth = false;
    Boolean ntpublishContoursreportPublishHeight = false;
    Boolean ntpublishContoursreportPublishSolidity = false;

    ntpublishContoursreport(ntpublishContoursreportData, ntpublishContoursreportName, ntpublishContoursreportPublishArea, ntpublishContoursreportPublishCenterx, ntpublishContoursreportPublishCentery, ntpublishContoursreportPublishWidth, ntpublishContoursreportPublishHeight, ntpublishContoursreportPublishSolidity, );
}




