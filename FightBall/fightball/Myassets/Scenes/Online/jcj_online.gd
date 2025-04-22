extends Control

# Nodos de la UI
@onready var lobby_list = $MarginContainer/MainContainer/ScrollContainer/LobbyList
@onready var button_create = $MarginContainer/MainContainer/HBoxContainer/ButtonCreate
@onready var button_back = $MarginContainer/MainContainer/HBoxContainer/ButtonBack
@onready var status_label = $MarginContainer/MainContainer/StatusLabel

var http := HTTPRequest.new()

func _ready():
	# Configuración inicial
	set_meta("scene_path", "res://Myassets/Scenes/Online/jcj_room_scene.tscn")
	add_child(http)
	status_label.text = "🔌 Conectando al servidor..."
	
	# Conexión de señales
	http.request_completed.connect(_on_lobby_list_received)
	
	# Solicitar lista de salas
	var headers = ["Accept: application/json", "Authorization: Bearer " + PlayerInfo.userKey]
	var error = http.request("http://localhost:8080/api/lobbies", headers)
	
	if error != OK:
		status_label.text = "❌ Error en la petición: %d" % error
		print("❌ HTTPRequest error:", error)

# Procesa la respuesta del servidor con la lista de salas
func _on_lobby_list_received(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray):
	http.queue_free()

	if response_code != 200:
		status_label.text = "❌ Error al obtener salas (HTTP %d)" % response_code
		return

	var json_text = body.get_string_from_utf8()
	var parsed = JSON.parse_string(json_text)

	if parsed is Array:
		clear_lobby_list()
		
		for room in parsed:
			var btn = Button.new()
			var name = room.get("name", "Sala")
			var players = room.get("players", 0)
			var capacity = room.get("capacity", 2)
			var room_id = room.get("id", -1)

			# Configurar botón de sala
			btn.text = "%s (%d/%d jugadores)" % [name, players, capacity]
			btn.size_flags_horizontal = Control.SIZE_EXPAND_FILL
			
			# Conectar señal para unirse a sala
			btn.pressed.connect(func():
				if players >= capacity:
					status_label.text = "🚫 Sala '%s' está llena." % name
					return
				
				status_label.text = "⏳ Uniéndose a sala: %s..." % name
				join_room(room_id)
			)
			
			lobby_list.add_child(btn)

		status_label.text = "✅ %d salas encontradas." % parsed.size()
	else:
		status_label.text = "⚠️ Respuesta malformada"

# Limpia la lista de salas
func clear_lobby_list():
	for child in lobby_list.get_children():
		lobby_list.remove_child(child)
		child.queue_free()

# Crea una nueva sala
func _on_ButtonCreate_pressed():
	status_label.text = "🛠️ Creando sala..."
	
	var http_create = HTTPRequest.new()
	add_child(http_create)
	
	http_create.request_completed.connect(func(_result, response_code, _headers, body):
		http_create.queue_free()
		if response_code == 201:
			var room = JSON.parse_string(body.get_string_from_utf8())
			status_label.text = "🟢 Sala '%s' creada. Esperando oponente..." % room["name"]
			join_room(room["id"])
		else:
			status_label.text = "❌ Error al crear sala"
	)
	
	# Datos para crear la sala
	var data = {
		"name": "Sala de " + PlayerInfo.userName,
		"players": 1,
		"capacity": 2
	}
	
	var error = http_create.request(
		"http://localhost:8080/api/lobbies",
		["Content-Type: application/json"],
		HTTPClient.METHOD_POST,
		JSON.stringify(data)
	)
	
	if error != OK:
		status_label.text = "❌ Error al enviar solicitud"

# Función para unirse a una sala (via WebSocket)
func join_room(room_id: int):
	# Guardar el ID de sala para usar en la escena de room
	PlayerInfo.current_room_id = room_id
	SceneManager.go_to("res://Myassets/Scenes/Online/jcj_room_scene.tscn")

# Volver al menú anterior
func _on_ButtonBack_pressed():
	SceneManager.go_back()
