extends Control

@onready var lobby_list = $MarginContainer/MainContainer/ScrollContainer/LobbyList  # Lista de salas en la interfaz
@onready var button_create = $MarginContainer/MainContainer/HBoxContainer/ButtonCreate  # Botón para crear una nueva sala
@onready var button_back = $MarginContainer/MainContainer/HBoxContainer/ButtonBack  # Botón para volver
@onready var status_label = $MarginContainer/MainContainer/StatusLabel  # Etiqueta de estado

var http := HTTPRequest.new()  # Instancia de HTTPRequest para realizar peticiones al servidor

func _ready():
	# Al iniciar la escena, establecemos la ruta de la escena a la que iremos después
	set_meta("scene_path", "res://Myassets/Scenes/Online/jcj_room_scene.tscn")
	add_child(http)  # Añadimos el HTTPRequest como hijo de la escena
	status_label.text = "🔌 Conectando al servidor..."  # Mostramos mensaje de conexión

	# Conectamos la señal cuando se reciba la respuesta de la lista de salas
	http.request_completed.connect(_on_lobby_list_received)

	# Preparamos las cabeceras para la solicitud (incluyendo el token de autorización)
	var headers = ["Accept: application/json", "Authorization: Bearer " + PlayerInfo.userKey]
	# Realizamos la solicitud GET para obtener la lista de salas
	var error = http.request("http://localhost:8080/api/lobbies", headers)

	if error != OK:
		status_label.text = "❌ Error en la petición: %d" % error  # Mostramos un error si no se pudo realizar la solicitud
		print("❌ HTTPRequest error:", error)

# Función llamada cuando recibimos la respuesta de la lista de salas
func _on_lobby_list_received(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray):
	http.queue_free()  # Liberamos la instancia de HTTPRequest una vez que ya no la necesitamos

	# Verificamos si la respuesta fue exitosa (código 200)
	if response_code != 200:
		status_label.text = "❌ Error al obtener salas (HTTP %d)" % response_code
		return

	var json_text = body.get_string_from_utf8()  # Convertimos el cuerpo de la respuesta a texto
	var parsed = JSON.parse_string(json_text)  # Parseamos el texto JSON recibido

	if parsed is Array:
		clear_lobby_list()  # Limpiamos la lista de salas actual
		
		# Iteramos sobre cada sala recibida
		for room in parsed:
			var btn = Button.new()  # Creamos un nuevo botón para cada sala
			var name = room.get("name", "Sala")  # Nombre de la sala
			var players = room.get("players", 0)  # Número de jugadores en la sala
			var capacity = room.get("capacity", 2)  # Capacidad máxima de jugadores
			var room_id = room.get("id", -1)  # ID de la sala
			var ip_address = room.get("ip", "127.0.0.1")  # IP de la sala (predeterminada a localhost)

			# Establecemos el texto del botón
			btn.text = "%s (%d/%d jugadores)" % [name, players, capacity]
			btn.size_flags_horizontal = Control.SIZE_EXPAND_FILL  # Hacemos que el botón se expanda horizontalmente

			# Conectamos la acción que ocurre cuando el botón es presionado
			btn.pressed.connect(func():
				# Si la sala está llena, mostramos un mensaje de error
				if players >= capacity:
					status_label.text = "🚫 Sala '%s' está llena." % name
					return
				
				# Si no está llena, nos unimos a la sala
				status_label.text = "⏳ Uniéndose a sala: %s..." % name
				PlayerInfo.is_host = false
				PlayerInfo.current_room_id = room_id
				PlayerInfo.host_ip = ip_address
				
				# Creamos una conexión ENet para un cliente
				var peer := ENetMultiplayerPeer.new()
				peer.create_client("127.0.0.1", 7777)  # Cambia IP si no es local
				multiplayer.multiplayer_peer = peer
				
				# Cambiamos de escena a la sala de juego
				SceneManager.go_to("res://Myassets/Scenes/Online/jcj_room_scene.tscn")
			)

			# Añadimos el botón a la lista de salas
			lobby_list.add_child(btn)

		# Mostramos cuántas salas fueron encontradas
		status_label.text = "✅ %d salas encontradas." % parsed.size()
	else:
		status_label.text = "⚠️ Respuesta malformada"  # Si el formato de la respuesta es incorrecto

# Limpiamos la lista de salas actual
func clear_lobby_list():
	for child in lobby_list.get_children():
		lobby_list.remove_child(child)
		child.queue_free()

# Función llamada cuando se presiona el botón para crear una sala
func _on_ButtonCreate_pressed():
	status_label.text = "🛠️ Creando sala..."  # Mostramos mensaje de creación
# Creamos una conexión ENet para un servidor
	var peer := ENetMultiplayerPeer.new()
	peer.create_server(7777, 2)  # Creamos un servidor en el puerto 7777
	multiplayer.multiplayer_peer = peer
				# Cambiamos de escena a la sala de juego
	SceneManager.go_to("res://Myassets/Scenes/Online/jcj_room_scene.tscn")

	#var http_create = HTTPRequest.new()  # Creamos una nueva instancia de HTTPRequest
	#add_child(http_create)  # La añadimos a la escena
#
	## Conectamos la señal de respuesta para la creación de la sala
	#http_create.request_completed.connect(func(_result, response_code, _headers, body):
		#http_create.queue_free()  # Liberamos la instancia de HTTPRequest
#
		## Si la creación fue exitosa (código 201)
		#if response_code == 201:
			#var room = JSON.parse_string(body.get_string_from_utf8())  # Parseamos el cuerpo de la respuesta
			#status_label.text = "🟢 Sala '%s' creada. Esperando oponente..." % room["name"]
			#PlayerInfo.is_host = true
			#PlayerInfo.current_room_id = room["id"]
			#
			## Creamos una conexión ENet para un servidor
			#var peer := ENetMultiplayerPeer.new()
			#peer.create_server(7777, 2)  # Creamos un servidor en el puerto 7777
			#multiplayer.multiplayer_peer = peer
			#
			## Cambiamos de escena a la sala de juego
			#SceneManager.go_to("res://Myassets/Scenes/Online/jcj_room_scene.tscn")
		#else:
			#status_label.text = "❌ Error al crear sala"  # Si hubo un error al crear la sala
	#)
#
	## Datos de la nueva sala
	#var data = {
		#"name": "Sala de " + PlayerInfo.userName,
		#"players": 1,
		#"capacity": 2,
		#"ip": "127.0.0.1"  # Esto debería ser la IP pública si vas online
	#}
#
	## Enviamos la solicitud POST para crear la sala
	#var error = http_create.request(
		#"http://localhost:8080/api/lobbies",
		#["Content-Type: application/json"],
		#HTTPClient.METHOD_POST,
		#JSON.stringify(data)
	#)
#
	#if error != OK:
		#status_label.text = "❌ Error al enviar solicitud"  # Si hubo un error al enviar la solicitud

# Función para volver a la escena anterior
func _on_ButtonBack_pressed():
	SceneManager.go_back()
