<?php

$dbName = "MyOpinion";
$user = "root";
$pwd = "Ig_97222028";
$host = "localhost";
$cnn = new PDO('mysql:dbname='.$dbName.';host='.$host, $user, $pwd);


define("ACTION_REGISTER","addUser");
define("ACTION_CHECK_USER","checkLogin");
define("ACTION_ADD_ESTABELECIMENTO","addEstabelecimento");
define("ACTION_GET_ALL_ESTABELECIMENTOS","getAllEstabelecimentos");
define("ACTION_GET_FAVORITES","getFavorites");
define("ACTION_GET_USER","getUser");
define("ACTION_GET_USERS","getUsers");
define("ACTION_GET_ESTABELECIMENTO","getEstabelecimento");
define("ACTION_GET_NEW_ESTABELECIMENTOS","getNewEstabelecimentos");
define("ACTION_GET_PROXIMOS","getProximos");
define("ACTION_UPDATE_RATE","updateRate");

define("RESULT_SUCCESS", 0);
define("RESULT_ERROR", 1);

foreach($_GET as $key=>$value)  $$key = $value;
foreach($_POST as $key=>$value) $$key = $value;

$result = RESULT_ERROR;

if(isset($action))
{
	switch ($action) {
		case ACTION_REGISTER:
			$result = addUser($cnn,$nome,$email,$dtNasc,$senha,$foto,$cep);
			echo($result);
			break;

		case ACTION_CHECK_USER:
			$result = checkLogin($cnn,$email,$senha);
			echo($result);
			break;

		case ACTION_GET_ALL_ESTABELECIMENTOS:
			$result = getAllEstabelecimentos($cnn);
			echo($result);
			break;

		case ACTION_GET_FAVORITES:
			$result = getFavorites($cnn,$listaFavoritos);
			echo($result);
			break;

		case ACTION_GET_USER:
			$result = getUser($cnn,$idusuario);
			echo($result);
			break;

		case ACTION_GET_ESTABELECIMENTO:
			$result = getEstabelecimento($cnn,$idEstabelecimento);
			echo(json_encode($result));
			break;

		case ACTION_ADD_ESTABELECIMENTO:
			$result = addEstabelecimento($cnn, $nome, $tpEstabelecimento,$latitude,$longitude,$notificacao,$foto,$rate,$avaliations,$estado,$cidade,$bairro,$idusuario,$responsavel);
			echo($result);
			break;

		case ACTION_GET_USERS:
			$result = getUsers($cnn);
			echo($result);
			break;

		case ACTION_GET_NEW_ESTABELECIMENTOS:
			$result = getNewEstabelecimentos($cnn);
			echo($result);
			break;

		case ACTION_GET_PROXIMOS:
			$result = getProximos($cnn,$latitude=0,$longitude=0);
			echo($result);
			break;

		case ACTION_UPDATE_RATE:
			$result = updateRate($cnn,$idEstabelecimento,$rate, $avaliations);
			echo($result);
			break;

		default:
			echo("OPÇÃO INVÁLIDA!");
			break;
	}
}


function getAllEstabelecimentos($cnn)
{
	$query = "SELECT * FROM estabelecimento ORDER BY rate DESC";
	$stmt = $cnn->prepare($query);
	$stmt->execute();
	$listEstabelecimentos = null;

	while($row = $stmt->fetch(PDO::FETCH_NUM,PDO::FETCH_ORI_NEXT))
	{
		$listEstabelecimentos[] = array(
										'_ID' => $row[0],
										'nome' => $row[1],
										'tipoEstabelecimento' => $row[2],
										'latitude' => $row[3],
										'longitude' => $row[4],
										'notificacao' => $row[5],
										'foto' => $row[6],
										'idUsuario'=> $row[7],
										'rate' => $row[8],
										'avaliations' => $row[9],
										'estado' => $row[10],
										'cidade' => $row[11],
										'bairro' => $row[12],
										'responsavel' => $row[13]
										);
	}
	return json_encode($listEstabelecimentos);
}


function getNewEstabelecimentos($cnn)
{
	$query = "SELECT * FROM estabelecimento ORDER BY idestabelecimento DESC LIMIT 5;";
	$stmt = $cnn->prepare($query);
	$stmt->execute();
	$listEstabelecimentos = null;

	while($row = $stmt->fetch(PDO::FETCH_NUM,PDO::FETCH_ORI_NEXT))
	{
		$listEstabelecimentos[] = array(
										'_ID' => $row[0],
										'nome' => $row[1],
										'tipoEstabelecimento' => $row[2],
										'latitude' => $row[3],
										'longitude' => $row[4],
										'notificacao' => $row[5],
										'foto' => $row[6],
										'idUsuario'=> $row[7],
										'rate' => $row[8],
										'avaliations' => $row[9],
										'estado' => $row[10],
										'cidade' => $row[11],
										'bairro' => $row[12],
										'responsavel' => $row[13]
										);
	}
	return json_encode($listEstabelecimentos);
}

function getEstabelecimento($cnn,$idEstabelecimento)
{
	$query = "SELECT * FROM estabelecimento WHERE idestabelecimento = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1,$idEstabelecimento);
	$stmt->execute();
	$novo = null;

	if($row = $stmt->fetch())
	{
		$novo = array(
						'_ID' => $row[0],
						'nome' => $row[1],
						'tipoEstabelecimento' => $row[2],
						'latitude' => $row[3],
						'longitude' => $row[4],
						'notificacao' => $row[5],
						'foto' => $row[6],
						'idUsuario'=> $row[7],
						'rate' => $row[8],
						'avaliations' => $row[9],
						'estado' => $row[10],
						'cidade' => $row[11],
						'bairro' => $row[12],
						'responsavel' => $row[13]
					);
	}
	return $novo;
}



function getFavorites($cnn,$listaFavoritos)
{
	$pieces = explode(";", $listaFavoritos);
	$listEstabelecimentos = null;
	foreach ($pieces as $value) {
		$listEstabelecimentos[] = getEstabelecimento($cnn,$value);
	}

	return json_encode($listEstabelecimentos);

}

function cmp($a,$b)
{
	if ($a['distance'] == $b['distance']) {
    return 0;
    }
    return ($a['distance'] < $b['distance']) ? -1 : 1;
}

function getProximos($cnn,$latitude,$longitude)
{
	$listEstabelecimentos = json_decode(getAllEstabelecimentos($cnn));
	$vector = null;
	$list = null;
	foreach ($listEstabelecimentos as $key => $value) {
		foreach ($value as $key => $value) {
			if ($key == '_ID'){ $id = $value; }
			if ($key == 'latitude'){ $latitudeTo = $value; }
			if ($key == 'longitude'){ $longitudeTo = $value; }
		}

		$distancia = haversineGreatCircleDistance($latitude, $longitude, $latitudeTo, $longitudeTo);
		$vector[] = array( 'id' => $id, 'distance'=>$distancia);
	}
	usort($vector, "cmp");

	foreach ($vector as $key => $value) {
		$list[] = getEstabelecimento($cnn,$value['id']);
	}

	return json_encode($list);
}

function addEstabelecimento($cnn, $nome, $tpEstabelecimento,$latitude,$longitude,$notificacao,$foto,$rate,$avaliations,$estado,$cidade,$bairro,$idusuario,$responsavel)
{
	$query = "INSERT INTO estabelecimento(nome, tipoEstabelecimento, latitude,longitude,notificacao,foto,usuario_idusuario,rate,avaliations,estado,cidade,bairro,responsavel ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1,$nome);
	$stmt->bindParam(2,$tpEstabelecimento);
	$stmt->bindParam(3,$latitude);
	$stmt->bindParam(4,$longitude);
	$stmt->bindParam(5,$notificacao);
	$stmt->bindParam(6,$foto);
	$stmt->bindParam(7,$idusuario);
	$stmt->bindParam(8,$rate);
	$stmt->bindParam(9,$avaliations);
	$stmt->bindParam(10,$estado);
	$stmt->bindParam(11,$cidade);
	$stmt->bindParam(12,$bairro);
	$stmt->bindParam(13,$responsavel);
	$stmt->execute();

	return RESULT_SUCCESS;
}


function updateRate($cnn,$idEstabelecimento,$rate, $avaliations)
{
	$query = "UPDATE estabelecimento SET rate = ?, avaliations = ? WHERE idestabelecimento = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1,$rate);
	$stmt->bindParam(2,$avaliations);
	$stmt->bindParam(3,$idEstabelecimento);
	$stmt->execute();
	return RESULT_SUCCESS;
}


/* ----- FUNÇÕES DE MANIPULAÇÃO PARA USUÁRIOS ----- */
function countUsers($cnn){

	$query = "SELECT * FROM usuario";
	$stmt = $cnn->prepare($query);
	$stmt->execute();
	return $stmt->rowCount();
}

function addUser($cnn,$nome,$email,$dtNasc,$senha,$foto,$cep)
{
	$id = countUsers($cnn);
	$query = "INSERT INTO usuario(idusuario,nome,email,dtNasc,senha,foto,cep) VALUES (?,?,?,?,?,?,?)";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1,$id);
	$stmt->bindParam(2,$nome);
	$stmt->bindParam(3,$email);
	$stmt->bindParam(4,$dtNasc);
	$stmt->bindParam(5,$senha);
	$stmt->bindParam(6,$foto);
	$stmt->bindParam(7,$cep);
	$stmt->execute();
	return RESULT_SUCCESS;

}

function checkLogin($cnn,$email,$senha)
{
	$query = "SELECT * FROM usuario WHERE email = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1,$email);
	$stmt->execute();

	$user = -1;
   	$row = $stmt->fetch();
   	if($row == null)
   	{
   		$user = -2;
   		return $user;
   	}

   	if($row[4] == $senha)
   	{
   		$user = $row[0];//array('_ID' => $row[0],'nome' => $row[1],'email' => $row[2], 'dtNasc' => $row[3], 'senha' => $row[4], 'foto'=>$row[5],'cep' =>$row[6]);
   	}

    return $user;
}

function getUsers($cnn)
{
	$query = "SELECT * FROM usuario";
	$stmt = $cnn->prepare($query);
	$stmt->execute();

	$users = null;
    while($row = $stmt->fetch(PDO::FETCH_NUM, PDO::FETCH_ORI_NEXT)){
        $users[] = array('_ID' => $row[0],'nome' => $row[1],'email' => $row[2], 'dtNasc' => $row[3], 'senha' => $row[4], 'foto'=>$row[5], 'cep' =>$row[6]);
    }

    return json_encode($users);
}

function getUser($cnn,$idusuario)
{
	$query = "SELECT * FROM usuario WHERE idUsuario = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1,$idusuario);
	$stmt->execute();

	$user = null;
	if($row = $stmt->fetch())
	{
		$user = array('_ID' => $row[0],'nome' => $row[1],'email' => $row[2], 'dtNasc' => $row[3], 'senha' => $row[4], 'foto'=>$row[5],'cep' =>$row[6]);
	}

    return json_encode($user);
}


/**
 * Calculates the great-circle distance between two points, with
 * the Haversine formula.
 * @param float $latitudeFrom Latitude of start point in [deg decimal]
 * @param float $longitudeFrom Longitude of start point in [deg decimal]
 * @param float $latitudeTo Latitude of target point in [deg decimal]
 * @param float $longitudeTo Longitude of target point in [deg decimal]
 * @param float $earthRadius Mean earth radius in [m]
 * @return float Distance between points in [m] (same as earthRadius)
 */
function haversineGreatCircleDistance($latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo,$earthRadius = 6371000)
{
  // convert from degrees to radians
  $latFrom = deg2rad($latitudeFrom);
  $lonFrom = deg2rad($longitudeFrom);
  $latTo = deg2rad($latitudeTo);
  $lonTo = deg2rad($longitudeTo);

  $latDelta = $latTo - $latFrom;
  $lonDelta = $lonTo - $lonFrom;

  $angle = 2 * asin(sqrt(pow(sin($latDelta / 2), 2) +
    cos($latFrom) * cos($latTo) * pow(sin($lonDelta / 2), 2)));
  return $angle * $earthRadius;
}

?>
