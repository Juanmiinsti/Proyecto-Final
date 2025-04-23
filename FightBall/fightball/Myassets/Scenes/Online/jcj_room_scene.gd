extends Control

# Nodos de la UI
@onready var player1_status = $VBoxContainer/Player1Status
@onready var player2_status = $VBoxContainer/Player2Status
@onready var ready_button = $ReadyButton
@onready var status_label = $StatusLabel

# Variables de conexión
var is_ready = false
var websocket := WebSocketPeer.new()
var is_connected := false

func _ready():
	# Configuración inicial
	set_meta("scene_path", "res://Myassets/Scenes/JCJOnline/JCjRoomScene.tscn")
	status_label.text = "🔌 Conectando a sala..."
	
	# Conectar al servidor WebSocket
	connect_to_server()

# Intenta conectar al servidor WebSocket
func connect_to_server():
	# Configurar headers de autenticación
	var options = PackedStringArray()
	options.append("Authorization: Bearer %s" % PlayerInfo.userKey)
	websocket.set_handshake_headers(options)
	
	# URL de conexión (sin ruta específica)
	var url = "ws://localhost:8080/ws-godot"
	var error = websocket.connect_to_url(url)
	
	if error != OK:
		status_label.text = "❌ Error al conectar"
		print("Error conectando WebSocket:", error)

func _process(_delta):
	websocket.poll()
	
	# Manejar conexión establecida
	if websocket.get_ready_state() == WebSocketPeer.STATE_OPEN and not is_connected:
		is_connected = true
		status_label.text = "✅ Conectado a la sala"
		# Enviar mensaje de unión después de conectar
		send_message({
			"type": "join_room",
			"user_id": str(PlayerInfo.userID),
			"room_id": str(PlayerInfo.current_room_id)
		})
	
	# Procesar mensajes recibidos
	while websocket.get_available_packet_count() > 0:
		var packet = websocket.get_packet().get_string_from_utf8()
		handle_message(packet)

# Envía un mensaje al servidor
func send_message(data: Dictionary):
	var json = JSON.stringify(data)
	print("➡️ Enviando mensaje WebSocket:", json)

	websocket.put_packet(json.to_utf8_buffer())

# Procesa mensajes recibidos del servidor
func handle_message(msg: String):
	var parsed = JSON.parse_string(msg)
	if typeof(parsed) != TYPE_DICTIONARY:
		print("⚠️ JSON inválido:", msg)
		return

	match parsed.get("type", ""):
		"player_joined":
			update_player_status(parsed["user_id"], false)
		"player_ready":
			update_player_status(parsed["user_id"], true)
			if parsed["user_id"] != str(PlayerInfo.userID):
				status_label.text = "⚔️ El oponente está listo. ¡Presiona tu botón cuando quieras!"
			else:
				status_label.text = "✅ Esperando al otro jugador..."
		"both_ready":
			start_character_selection()
		"error":
			show_error(parsed.get("message", "Error desconocido"))

# Actualiza el estado de un jugador
func update_player_status(user_id: String, is_ready: bool):
	if user_id != str(PlayerInfo.userID):
		player2_status.text = "Jugador 2: %s" % ["¡Listo ✅!" if is_ready else "Conectado ✅"]
	else:
		player1_status.text = "Jugador 1: %s" % ["¡Listo ✅!" if is_ready else "Conectado ✅"]

# Muestra errores al usuario
func show_error(message: String):
	status_label.text = "❌ Error: " + message
	# Opcional: Habilitar botón de reintento

# Inicia la selección de personaje
func start_character_selection():
	status_label.text = "🚀 Ambos jugadores listos. Cargando selección de personaje..."
	await get_tree().create_timer(1.5).timeout
	SceneManager.go_to("res://Myassets/Scenes/characterSelectionOnline.tscn")

# Botón para marcarse como listo
func _on_ReadyButton_pressed():
	if not is_ready:
		is_ready = true
		send_message({
			"type": "player_ready",
			"user_id": str(PlayerInfo.userID),
			"room_id": str(PlayerInfo.current_room_id)
		})
		#ready_button.disabled = true
