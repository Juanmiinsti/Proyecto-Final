extends Node2D

var wsUrl = "ws://localhost:8080/number"
var socket := WebSocketPeer.new()
var handshake_headers= PackedStringArray(["Authorization: Bearer " + PlayerInfo.userKey])
@onready var messageList=$TextureRect/ScrollContainer/messageList
@onready var input=$TextureRect/msgInput
signal added
@onready var scrollbar:ScrollContainer= $TextureRect/ScrollContainer


var connected=false;

func _ready() -> void:

	set_meta("scene_path", "res://numberGuess.tscn")
	
	socket.handshake_headers=handshake_headers
	var err = socket.connect_to_url(wsUrl)
	if err != OK:
		print("❌ Error al conectar:", err)
		set_process(false)
	else:
		print("⌛ Conectando...")



func _process(delta):
	socket.poll()

	if socket.get_ready_state() == WebSocketPeer.STATE_OPEN and connected==false:
		print("✅ ¡Conexión establecida!")
		socket.send_text(PlayerInfo.userName + ":connected")
		connected=true

	if socket.get_available_packet_count() > 0:
		var packet = socket.get_packet().get_string_from_utf8()
		print("📩 Paquete recibido:", packet)
		add_message(packet)
		
func _scroll_to_bottom():
	scrollbar.get_v_scroll_bar().value = scrollbar.get_v_scroll_bar().max_value

		
func _exit_tree():
	if socket.get_ready_state() == WebSocketPeer.STATE_OPEN:
		socket.close(1000, "Scene exit")

func _on_go_back_pressed() -> void:
	SceneManager.go_back() 
	# Replace with function body.
	
func _on_send_pressed():
	var guess = input.text.strip_edges()
	if guess.is_valid_int():
		var msg = PlayerInfo.userName + ":" + guess
		socket.send_text(msg)
		input.clear()
	else:
		add_message("⚠️ Introduce un número válido")	
	
	
func add_message(text: String):
	var bg = NinePatchRect.new()
	bg.texture = load("res://Myassets/Img/fondoTutorial.png") # tu imagen de fondo para tutorial
	bg.custom_minimum_size = Vector2(0, 60)
	bg.patch_margin_left = 140
	bg.patch_margin_top = 5
	bg.patch_margin_right = 140
	bg.patch_margin_bottom = 5# ajustá esto según los bordes de tu imagen

	var content = VBoxContainer.new()
	content.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)


	var desc_label = RichTextLabel.new()
	desc_label.text = text
	desc_label.add_theme_color_override("font_color", Color.BLACK)
	desc_label.fit_content = true
	desc_label.scroll_active = false
	content.add_child(desc_label)

	bg.add_child(content)
	
	messageList.add_child(bg)

	var separator = HSeparator.new()
	messageList.add_child(separator)
	
	call_deferred("_scroll_to_bottom")
