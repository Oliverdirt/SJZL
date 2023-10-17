<template>
  <Alert type="warning" show-icon closable>
    输入框样式规范：
    <template slot="desc">
    <p>1.冒号对齐，整体居中对齐</p>
    <p>2.合理定宽，输入框宽度应该向用户暗示所需输入内容的长度。宽度要求：</p>
    <p>&nbsp;&nbsp;XS：104px; 适用于短数字、超短文本或选项</p>
    <p>&nbsp;&nbsp;S &nbsp;：&nbsp;216px; 适用于较短文本或选项录入，如姓名、电话、邮箱、身份证、ID等</p>
    <p>&nbsp;&nbsp;M ：&nbsp;328px; 标准输入框宽度、适用于大部分字段长度</p>
    <p>&nbsp;&nbsp;L &nbsp;：&nbsp;440px; 适用于较长文字端录入，如较长网址、标签组、文件路径、数据名称等</p>
    <p>&nbsp;&nbsp;XL：&nbsp;552px; 适用于长字段录入，如长链接、正文（描述、摘要、简介、备注）等，可以预留多行空间</p>
    <p>3.输入框高度统一为32px</p>
    <p>4.不同字段上下间距24px</p>
    <p>注：当前示例仅为样式示例</p>
  </template>
  </Alert>
</template>
