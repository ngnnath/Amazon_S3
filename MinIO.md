---


---

<h2 id="minio">MinIO</h2>
<p>Server</p>
<pre><code>docker run -p 9000:9000 --name minio1 -e "MINIO_ACCESS_KEY=root" -e "MINIO_SECRET_KEY=password" -v /home/ngnnath/minio/data:/data -v /home/ngnnath/minio/config:/root/.minio minio/minio server /data
</code></pre>
<p>MinIO   Client</p>
<pre><code>wget https://dl.min.io/client/mc/release/linux-amd64/mc
chmod +x mc
./mc --help
</code></pre>
<p>Create a bucket</p>
<pre><code>./mc mb myS3/mybucket
</code></pre>

