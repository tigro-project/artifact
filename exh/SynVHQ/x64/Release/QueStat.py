import pandas as pd
import numpy as np
import glob
import os

def weighted_percentile(values: np.ndarray, weights: np.ndarray, pct: float) -> float:
    """
    Compute the weighted percentile of `values` using `weights`, with linear interpolation.

    1. Sort `values` and their corresponding `weights` in ascending order of `values`.
    2. Build the cumulative sum of weights: cumw[i] = sum(weights[:i+1]).
    3. Let total = cumw[-1], cutoff = pct * total.
    4. Find the smallest index `i` such that cumw[i] >= cutoff.
       - If i == 0, return v_sorted[0].
       - Otherwise:
           prev_cum = cumw[i-1]
           prev_val = v_sorted[i-1]
           curr_val = v_sorted[i]
           curr_w   = weights_sorted[i]
           fraction = (cutoff - prev_cum) / curr_w
           return prev_val + fraction * (curr_val - prev_val)
    This linearly interpolates between v_sorted[i-1] and v_sorted[i].
    """
    # 1) Sort by values
    sorter = np.argsort(values)
    v_sorted = values[sorter]
    w_sorted = weights[sorter]

    # 2) Compute cumulative sum of weights
    cumw = np.cumsum(w_sorted)
    total_weight = cumw[-1]
    if total_weight == 0:
        # If all weights are zero (shouldn't happen if we filtered out w > 0), return first value
        return float(v_sorted[0])

    cutoff = pct * total_weight

    # 3) Locate the index i where cumw[i] >= cutoff
    i = np.searchsorted(cumw, cutoff)

    # 4) If i == 0, nothing to interpolate: return the smallest value
    if i == 0:
        return float(v_sorted[0])

    # 5) Otherwise, linearly interpolate between v_sorted[i-1] and v_sorted[i]
    prev_cum = cumw[i - 1]
    prev_val = v_sorted[i - 1]
    curr_val = v_sorted[i]
    curr_w   = w_sorted[i]

    # If curr_w is zero (rare if we filtered out zero‐weights), just return curr_val
    if curr_w == 0:
        return float(curr_val)

    fraction = (cutoff - prev_cum) / curr_w
    return float(prev_val + fraction * (curr_val - prev_val))


def compute_weighted_queue_metrics(df: pd.DataFrame) -> dict:
    """
    Given a DataFrame with columns:
      - "QueueSize"  (int or castable to int)
      - "DeltaTime"  (float, e.g. microseconds spent in that queue size),
    compute and return a dict containing:
      - weighted_mean      : time-weighted mean of QueueSize
      - weighted_median    : time-weighted median of QueueSize
      - weighted_75pct     : time-weighted 75th percentile of QueueSize
      - max_queuesize      : ordinary maximum of QueueSize
      - weighted_std       : time-weighted standard deviation of QueueSize
    """
    # 1) Drop rows missing required columns
    df = df.dropna(subset=["QueueSize", "DeltaTime"])

    # 2) Cast types, and filter out non-positive weights
    df["QueueSize"] = df["QueueSize"].astype(int)
    df["DeltaTime"] = df["DeltaTime"].astype(float)
    df = df[df["DeltaTime"] > 0.0]

    # 3) Extract arrays
    qs = df["QueueSize"].to_numpy(dtype=float)
    w  = df["DeltaTime"].to_numpy(dtype=float)
    wsum = w.sum()
    if wsum == 0:
        # All weights are zero (unlikely, since we filtered), but guard anyway.
        return {
            "weighted_mean":    0.0,
            "weighted_median":  0.0,
            "weighted_75pct":   0.0,
            "max_queuesize":    float(df["QueueSize"].max()) if len(df) else 0.0,
            "weighted_std":     0.0
        }

    # 4) Weighted mean
    wmean = float(np.dot(qs, w) / wsum)

    # 5) Weighted median (50th percentile)
    wmed = weighted_percentile(qs, w, 0.50)

    # 6) Weighted 75th percentile
    w75 = weighted_percentile(qs, w, 0.75)

    # 7) Weighted standard deviation
    var = np.dot(w, (qs - wmean) ** 2) / wsum
    wstd = float(np.sqrt(var))

    # 8) Ordinary maximum of QueueSize
    qmax = float(df["QueueSize"].max())

    return {
        "weighted_mean":    wmean,
        "weighted_median":  wmed,
        "weighted_75pct":   w75,
        "max_queuesize":    qmax,
        "weighted_std":     wstd
    }


def main():
    # 1) Find all CSVs whose filename contains "Queue" (case-sensitive)
    files = sorted(glob.glob("../I9/Latency_data_10_4_2/*Queue*.csv"))
    if not files:
        print("No files matching '*Queue*.csv' found in the current directory.")
        return

    for filepath in files:
        try:
            df = pd.read_csv(filepath)
        except Exception as e:
            print(f"❌ Skipping '{filepath}': could not read CSV ({e})")
            continue

        # 2) Check for required columns
        if not {"QueueSize", "DeltaTime"}.issubset(df.columns):
            print(f"❌ Skipping '{filepath}': missing 'QueueSize' and/or 'DeltaTime' columns")
            continue

        # 3) Compute metrics
        metrics = compute_weighted_queue_metrics(df)

        # 4) Print results
        print(f"\n=== Metrics for '{os.path.basename(filepath)}' ===")
        print(f"Weighted mean   QueueSize : {metrics['weighted_mean']:.4f}")
        print(f"Weighted median QueueSize : {metrics['weighted_median']:.4f}")
        print(f"Weighted 75th‐pct         : {metrics['weighted_75pct']:.4f}")
        print(f"Max QueueSize observed    : {metrics['max_queuesize']:.0f}")
        print(f"Weighted std‐dev of QSize : {metrics['weighted_std']:.4f}")


if __name__ == "__main__":
    main()
