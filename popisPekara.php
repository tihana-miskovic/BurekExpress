<?php

$host="localhost"; // Host name
$username="root"; // Mysql username
$password=""; // Mysql password
$db_name="android"; // Database name
$tbl_name="pekara"; // Table name
$tbl_name2="mjesto"; // Table name
$tbl_name3="rvrijeme"; // Table name


// Connect to server and select database.
mysql_connect("$host", "$username", "$password")or die("cannot connect");
mysql_select_db("$db_name")or die("cannot select DB");


$akcija=$_GET['akcija'];
$mjesto=$_GET['mjesto'];
$pekara=$_GET['pekara'];
$vrijeme=$_GET['vrijeme'];


// To protect MySQL injection
$akcija = mysql_real_escape_string($akcija);
$mjesto = mysql_real_escape_string($mjesto);
$pekara = mysql_real_escape_string($pekara);

//echo $akcija;
//echo $mjesto;
//echo $pekara;
//echo $vrijeme;


if ($akcija=='Apopis1') {

$sql="SELECT * FROM $tbl_name JOIN $tbl_name2 JOIN $tbl_name3 WHERE pekara.pbr=mjesto.pbr AND pekara.id=rvrijeme.id AND grad='$mjesto' AND '$vrijeme' > rvrijeme.rvTod AND '$vrijeme' < rvrijeme.rvTdo";
$result=mysql_query($sql);

while($row=mysql_fetch_array($result))
  $output[]=$row;
 print(json_encode($output));
 mysql_close();  
 
}



elseif ($akcija=='Apopis2') {

$sql="SELECT * FROM $tbl_name JOIN $tbl_name2 JOIN $tbl_name3 WHERE pekara.pbr=mjesto.pbr AND pekara.id=rvrijeme.id AND grad='$mjesto' AND '$vrijeme' > rvrijeme.rvSod AND '$vrijeme' < rvrijeme.rvSdo";
$result=mysql_query($sql);

while($row=mysql_fetch_array($result))
  $output[]=$row;
 print(json_encode($output));
 mysql_close();  
 
}



elseif ($akcija=='Apopis3') {

$sql="SELECT * FROM $tbl_name JOIN $tbl_name2 JOIN $tbl_name3 WHERE pekara.pbr=mjesto.pbr AND pekara.id=rvrijeme.id AND grad='$mjesto' AND '$vrijeme' BETWEEN rvrijeme.rvNod AND rvrijeme.rvNdo";
$result=mysql_query($sql);

while($row=mysql_fetch_array($result))
  $output[]=$row;
 print(json_encode($output));
 mysql_close(); 
  
}



elseif ($akcija=='Bpopis1') {

$sql="SELECT * FROM $tbl_name JOIN $tbl_name2 JOIN $tbl_name3 WHERE pekara.pbr=mjesto.pbr AND pekara.id=rvrijeme.id AND grad='$mjesto' AND '$vrijeme' > rvrijeme.rvTod AND '$vrijeme' < rvrijeme.rvTdo AND pekara.nazPekara='$pekara'";
$result=mysql_query($sql);

while($row=mysql_fetch_array($result))
  $output[]=$row;
 print(json_encode($output));
 mysql_close(); 

}



elseif ($akcija=='Bpopis2') {

$sql="SELECT * FROM $tbl_name JOIN $tbl_name2 JOIN $tbl_name3 WHERE pekara.pbr=mjesto.pbr AND pekara.id=rvrijeme.id AND grad='$mjesto' AND '$vrijeme' > rvrijeme.rvSod AND '$vrijeme' < rvrijeme.rvSdo AND pekara.nazPekara='$pekara'";
$result=mysql_query($sql);

while($row=mysql_fetch_array($result))
  $output[]=$row;
 print(json_encode($output));
 mysql_close(); 

 }
 
elseif ($akcija=='Bpopis3') {

$sql="SELECT * FROM $tbl_name JOIN $tbl_name2 JOIN $tbl_name3 WHERE pekara.pbr=mjesto.pbr AND pekara.id=rvrijeme.id AND grad='$mjesto' AND '$vrijeme' BETWEEN rvrijeme.rvNod AND rvrijeme.rvNdo AND pekara.nazPekara='$pekara'";
$result=mysql_query($sql);

while($row=mysql_fetch_array($result))
  $output[]=$row;
 print(json_encode($output));
 mysql_close(); 
}

 
 
?>