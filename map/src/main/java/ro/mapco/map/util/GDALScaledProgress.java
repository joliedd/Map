package ro.mapco.map.util;

import org.gdal.gdal.ProgressCallback;

/* Note : this is the most direct port of ogr2ogr.cpp possible */
/* It could be made much more java'ish ! */

class GDALScaledProgress extends ProgressCallback
{
    private double pctMin;
    private double pctMax;
    private ProgressCallback mainCbk;

    public GDALScaledProgress(double pctMin, double pctMax,
                              ProgressCallback mainCbk)
    {
        this.pctMin = pctMin;
        this.pctMax = pctMax;
        this.mainCbk = mainCbk;
    }

    public int run(double dfComplete, String message)
    {
        return mainCbk.run(pctMin + dfComplete * (pctMax - pctMin), message);
    }
};