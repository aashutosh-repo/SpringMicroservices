-- Upload metadata
CREATE TABLE upload_metadata (
  id BIGSERIAL PRIMARY KEY,
  file_name TEXT NOT NULL,
  storage_path TEXT NOT NULL,       -- s3://... or /tmp/...
  uploaded_by VARCHAR(128),
  uploaded_at DATETIME(6) NOT NULL DEFAULT now(),
  file_size_bytes BIGINT,
  job_execution_id BIGINT,          -- Spring Batch job id
  status VARCHAR(32) NOT NULL,      -- SUBMITTED / RUNNING / COMPLETED / FAILED
  total_rows BIGINT DEFAULT 0,
  processed_rows BIGINT DEFAULT 0,
  success_rows BIGINT DEFAULT 0,
  failed_rows BIGINT DEFAULT 0,
  message TEXT
);

-- errors stored per row
CREATE TABLE upload_errors (
  id BIGSERIAL PRIMARY KEY,
  upload_id BIGINT REFERENCES upload_metadata(id),
  line_number BIGINT,
  raw_line TEXT,
  error_code VARCHAR(64),
  error_message TEXT,
  created_at DATETIME(6) NOT NULL DEFAULT now()
);
