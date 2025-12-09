import pandas as pd
import numpy as np
import glob
import os

def weighted_percentile(values: np.ndarray, weights: np.ndarray, pct: float) -> float:
    sorter   = np.argsort(values)
    v_sorted = values[sorter]
    w_sorted = weights[sorter]
    cumw     = np.cumsum(w_sorted)
    cutoff   = pct * cumw[-1]
    idx      = np.searchsorted(cumw, cutoff)
    return float(v_sorted[idx])

def compute_latency_metrics(df: pd.DataFrame) -> dict:
    # … (earlier cleaning steps remain the same) …

    df = df.dropna(subset=["ResponseLength", "Latency"])
    df["ResponseLength"] = pd.to_numeric(df["ResponseLength"], errors="coerce").astype(float)
    df["Latency"]        = pd.to_numeric(df["Latency"], errors="coerce").astype(float)
    df = df[df["ResponseLength"] > 0.0].reset_index(drop=True)

    # ---------------------------------------------
    # total_response_sum: jump by each resp[i]
    resp = df["ResponseLength"].to_numpy(dtype=float)
    total_response_sum = 0.0
    i = 0
    n = len(resp)
    while i < n:
        L = int(resp[i])
        total_response_sum += L
        i += L
    # For [3,3,3,3,3,3,1,1,1], this does: 
    #   i=0→ L=3, sum=3, i→3
    #   i=3→ L=3, sum=6, i→6
    #   i=6→ L=1, sum=7, i→7
    #   i=7→ L=1, sum=8, i→8
    #   i=8→ L=1, sum=9, i→9→stop

    num_records = int(n)

    latencies = df["Latency"].to_numpy(dtype=float)
    weights   = df["ResponseLength"].to_numpy(dtype=float)
    wsum      = weights.sum()

    wmean = float(np.dot(latencies, weights) / wsum)
    wmed  = weighted_percentile(latencies, weights, 0.50)
    w75   = weighted_percentile(latencies, weights, 0.75)
    lat_max = float(df["Latency"].max())
    var   = np.dot(weights, (latencies - wmean) ** 2) / wsum
    wstd  = float(np.sqrt(var))

    return {
        "total_response_sum":      total_response_sum,
        "num_records":             num_records,
        "weighted_mean_latency":   wmean,
        "weighted_median_latency": wmed,
        "weighted_75pct_latency":  w75,
        "max_latency":             lat_max,
        "weighted_std_latency":    wstd
    }


def main():
    files = sorted(glob.glob("../i9/Latency_data_10_4_2/latency_data*.csv"))
    if not files:
        print("No files matching '*latency*.csv'.")
        return

    for filepath in files:
        try:
            df = pd.read_csv(filepath)
        except Exception as e:
            print(f"Skipping '{filepath}': cannot read CSV ({e})")
            continue

        if not {"ResponseLength", "Latency"}.issubset(df.columns):
            print(f"Skipping '{filepath}': missing required columns")
            continue

        metrics = compute_latency_metrics(df)
        print(f"\n=== Metrics for '{os.path.basename(filepath)}' ===")
        print(f"Total sum of Response values : {metrics['total_response_sum']:.0f}")
        print(f"Number of records (rows)     : {metrics['num_records']}")
        print(f"Weighted mean Latency        : {metrics['weighted_mean_latency']:.4f}")
        print(f"Weighted median Latency      : {metrics['weighted_median_latency']:.4f}")
        print(f"Weighted 75th-pct Latency    : {metrics['weighted_75pct_latency']:.4f}")
        print(f"Max Latency observed         : {metrics['max_latency']:.0f}")
        print(f"Weighted std-dev Latency     : {metrics['weighted_std_latency']:.4f}")

if __name__ == "__main__":
    main()
