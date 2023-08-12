const fs = require('fs');
const archiver = require('archiver');

// 创建一个输出流，将压缩包输出到 dist.zip
const output = fs.createWriteStream(__dirname + '/h5.zip');

// 创建一个 archiver 实例，指定输出流类型和压缩格式
const archive = archiver('zip', {
    zlib: { level: 9 } // 设置压缩级别
});

// 将输出流对接到 archiver 实例中 
archive.pipe(output);
 
// 将 dist 文件夹内的内容添加到压缩包中
archive.directory('dist', false);

// 完成后关闭输出流
archive.finalize();

// 压缩完成后的回调函数
output.on('close', () => {
  console.log('Zip completed!');
});