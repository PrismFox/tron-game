from flask import Flask, request
import time

app = Flask(__name__)

players = []
countdown_time = time.time_ns()

class Player():
	def __init__(self, id):
		self.id = id
		self.direction = 0

def get_all_players():
	return {"players": [{'direction': player.direction, 'id': player.id} for player in players if player is not None]}

def get_player(id):
	if players[id] is not None:
		return {'direction': players[id].direction, 'id': players[id].id}, 200
	else:
		return '', 404

def create_player():
	new_player = Player(len(players))
	players.append(new_player)
	return get_player(new_player.id)[0], 201

def delete_all_players():
	players.clear()
	return '', 204

def delete_player(id):
	if len(players)-1 < id or players[id] is None:
		return '', 404
	players[id] = None
	return '', 204

def set_countdown_time(nanoseconds):
	global countdown_time
	countdown_time = time.time_ns() + nanoseconds
	return '', 200

def get_remaining_countdown_time():
	curr_time = time.time_ns()
	if curr_time > countdown_time:
		ns = -1
	else:
		ns = countdown_time - curr_time
	return {'nanoseconds': ns}, 200

@app.route("/players", methods=["GET", "PUT", "DELETE"])
def all_players_api():
	if request.method == "GET" :
		return get_all_players()
	if request.method == "PUT":
		return create_player()
	if request.method == "DELETE":
		return delete_all_players()

@app.route("/players/<int:player_id>", methods=["GET", "DELETE"])
def player_api(player_id):
	if request.method == "GET":
		return get_player(player_id)
	if request.method == "DELETE":
		return delete_player(player_id)

@app.route("/players/<int:player_id>/direction", methods=["GET", "POST"])
def direction_api(player_id):
	if request.method == "GET":
		return {'direction': players[player_id].direction}, 200
	if request.method == "POST":
		if get_player(player_id)[1] != 404:
			players[player_id].direction = request.get_json(force=True)['direction']
			return '', 200

@app.route("/countdown", methods=["GET","PUT"])
def countdown_api():
	if(request.method == "PUT"):
		return set_countdown_time(int(request.get_json(force=True)["nanoseconds"]))
	if(request.method == "GET"):
		return get_remaining_countdown_time()
